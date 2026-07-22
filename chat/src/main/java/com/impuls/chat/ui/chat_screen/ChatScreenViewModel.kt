package com.impuls.chat.ui.chat_screen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.chat.domain.model.DownloadTypeFromUseCase
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.TmpMessage
import com.impuls.chat.domain.model.WebSocketChatMessage
import com.impuls.chat.domain.repository.AuthUseCase
import com.impuls.chat.domain.repository.DataStoreChatRepository
import com.impuls.chat.domain.repository.DownloadAnsSaveUseCase
import com.impuls.chat.domain.repository.FileRepository
import com.impuls.chat.domain.repository.NotificationRepository
import com.impuls.chat.domain.repository.RemoteChatRepository
import com.impuls.chat.domain.repository.WebSocketChatRepository
import com.impuls.chat.domain.utils.ResultWork
import com.impuls.chat.domain.utils.Utils
import com.impuls.chat.ui.chat_screen.ChatScreenEvent.NavigateToLogin
import com.impuls.chat.ui.chat_screen.ChatScreenEvent.OnMessage
import com.impuls.chat.ui.chat_screen.ChatScreenEvent.OpenAnotherApp
import com.impuls.chat.ui.navigation.NavigationChatRoute
import com.impuls.chat.ui.uikit.asUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val remoteRepository: RemoteChatRepository,
    private val authUseCase: AuthUseCase,
    private val webSocketChatRepository: WebSocketChatRepository,
    private val notificationRepository: NotificationRepository,
    private val dataStoreRepository: DataStoreChatRepository,
    private val downloadAnsSaveUseCase: DownloadAnsSaveUseCase,
    private val fileRepository: FileRepository,
) : ViewModel() {

    private val routeData = savedStateHandle.toRoute<NavigationChatRoute.ChatRoute>()

    private val _state = MutableStateFlow(
        ChatScreenState(
            name = routeData.name,
            login = routeData.login,
            password = routeData.password,
            email = routeData.email,
            billingToken = routeData.billingToken,
        )
    )
    val state = _state
        .onStart {
            initializeData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatScreenState()
        )

    init {
        observeToken()
    }

    private var webSocketJob: Job? = null
    private val eventChannel = Channel<ChatScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun initializeData() {
        viewModelScope.launch {
            withAuth { token ->
                getChatById(token)
                startWebSocket(token)
            }
        }
    }

    fun onAction(action: ChatScreenAction) {
        viewModelScope.launch {
            when (action) {
                is ChatScreenAction.SendMessage -> {
                    withAuth {
                        sendMessage(
                            accessToken = it,
                            message = action.message,
                            uriFiles = _state.value.attachedFiles
                        )
                    }
                }

                ChatScreenAction.CloseChat -> {
                    closeChat()
                }

                is ChatScreenAction.UpdateCurrentMessage -> {
                    _state.update { it.copy(currentMessage = action.message) }
                }

                is ChatScreenAction.AttachFile -> {
                    _state.update { it.copy(attachedFiles = it.attachedFiles + action.uri) }
                }

                is ChatScreenAction.RemoveFile -> {
                    _state.update { it.copy(attachedFiles = it.attachedFiles - action.uri) }
                }

                is ChatScreenAction.SetMessageAsRead -> {
                    withAuth {
                        markMessageAsRead(
                            messageId = action.messageId,
                            accessToken = it
                        )
                    }
                }

                is ChatScreenAction.ViewMedia -> {
                    _state.update { it.copy(selectedFile = action.file) }
                }

                is ChatScreenAction.OpenFileAnotherApp -> {
                    withAuth {
                        openFile(
                            fileName = action.fileName,
                            originalName = action.originalName,
                            mimeType = action.mimeType,
                            accessToken = it
                        )
                    }
                }

                is ChatScreenAction.SaveFileLocal -> {
                    withAuth {
                        downloadFile(
                            url = action.fileName,
                            fileName = action.originalName,
                            accessToken = it
                        )
                    }
                }
            }
        }
    }

    private fun observeToken() {
        viewModelScope.launch {
            dataStoreRepository.getToken().collectLatest { collector ->
                _state.update {
                    it.copy(
                        token = collector?.access ?: ""
                    )
                }
            }
        }
    }

    private suspend fun withAuth(
        action: suspend (String) -> Unit,
    ) {
        val currentState = _state.value
        _state.update { it.copy(isLoading = true) }
        authUseCase.invoke(
            function = {
                action(it)
            },
            onError = {
                Log.e("ChatScreenVM", "Auth error: $it")
                eventChannel.send(OnMessage(it.asUiText()))
            },
            name = currentState.name,
            username = currentState.login,
            email = currentState.email,
            password = currentState.password,
            billingToken = currentState.billingToken
        )
        _state.update { it.copy(isLoading = false) }
    }

    private suspend fun getChatById(accessToken: String) {
        _state.update { it.copy(isLoading = true) }
        when (val result = remoteRepository.getChatById(
            accessToken = accessToken,
            chatId = routeData.chatId
        )) {
            is ResultWork.Error -> {
                Log.e("ChatScreenVM", "getChatById error: ${result.error}")
                eventChannel.send(OnMessage(result.error.asUiText()))
            }

            is ResultWork.Success -> {
                _state.update { it.copy(chat = result.data) }
            }
        }
        _state.update { it.copy(isLoading = false) }
    }

    private suspend fun sendMessage(
        accessToken: String,
        message: String,
        uriFiles: List<Uri>
    ) {
        val newMessage = TmpMessage(urisFile = uriFiles)
        _state.update {
            it.copy(
                currentMessage = "",
                attachedFiles = emptyList(),
                tmpMessage = newMessage
            )
        }

        val files = uriFiles.mapNotNull { fileRepository.getFileFromUri(it) }

        when (val result = remoteRepository.sendMessage(
            accessToken = accessToken,
            chatId = routeData.chatId,
            message = message,
            files = files.ifEmpty { null }
        )) {
            is ResultWork.Error -> {
                eventChannel.send(OnMessage(result.error.asUiText()))
                _state.update {
                    it.copy(
                        tmpMessage = it.tmpMessage?.copy(statusMessage = StatusMessage.ERROR_SEND)
                    )
                }
            }

            is ResultWork.Success -> {
                _state.update {
                    it.copy(
                        tmpMessage = it.tmpMessage?.copy(statusMessage = StatusMessage.DELIVERED)
                    )
                }
            }
        }
    }

    private suspend fun closeChat() {
        webSocketJob?.cancel()
        webSocketChatRepository.close()
        eventChannel.send(NavigateToLogin)
    }

    private fun startWebSocket(accessToken: String) {
        webSocketJob?.cancel()
        webSocketJob = viewModelScope.launch {
            webSocketChatRepository.updateLiveChat(
                accessToken = accessToken,
                chatId = routeData.chatId
            ).collect { result ->
                when (result) {
                    is ResultWork.Error -> {
                        Log.e("ChatScreenVM", "WS error: ${result.error}")
                        eventChannel.send(OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        val data = result.data
                        if (data is WebSocketChatMessage.UpdateChatMessage) {
                            handleChatUpdate(data)
                        }
                    }
                }
            }
        }
    }

    private suspend fun handleChatUpdate(data: WebSocketChatMessage.UpdateChatMessage) {
    //    var newMsgs = listOf<Message>()
        _state.update { state ->
            val mergeResult = state.chat?.let { existingChat ->
                Utils.mergeChatUpdates(existingChat, data.chat)
            } ?: (data.chat to emptyList())

                //   newMsgs = mergeResult.second

            state.copy(
                tmpMessage = null,
                chat = mergeResult.first
            )
        }

       /* newMsgs.forEach { message ->
            notificationRepository.showNotification(
                title = data.chat.group.name,
                message = message.text,
                chatId = data.chat.id,
                data = mapOf("chatId" to data.chat.id)
            )
        }*/
    }

    private suspend fun markMessageAsRead(
        messageId: String,
        accessToken: String
    ) {
        when (val result = remoteRepository.markMessageAsRead(
            accessToken = accessToken,
            messageId = messageId
        )) {
            is ResultWork.Error -> {
                Log.e("ChatScreenVM", "markAsRead error: ${result.error}")
                eventChannel.send(OnMessage(result.error.asUiText()))
            }

            is ResultWork.Success -> {}
        }
    }

    private suspend fun openFile(
        fileName: String,
        mimeType: MimeTypes.MimeType,
        accessToken: String,
        originalName: String
    ) {
        downloadAnsSaveUseCase.invoke(
            accessToken = accessToken,
            originalName = originalName,
            fileName = fileName
        ).collect { collector ->
            when (collector) {
                is DownloadTypeFromUseCase.Failed -> {
                    eventChannel.send(OnMessage(collector.error.asUiText()))
                }

                is DownloadTypeFromUseCase.Finished -> {
                    eventChannel.send(
                        OpenAnotherApp(
                            uri = collector.uri,
                            mimeType = mimeType
                        )
                    )
                }

                is DownloadTypeFromUseCase.Progress -> {
                    _state.update {
                        it.copy(
                            progressDownload = collector.value
                        )
                    }
                }

                DownloadTypeFromUseCase.Started -> {
                    _state.update {
                        it.copy(
                            progressDownload = ""
                        )
                    }
                }
            }
        }
    }

    private suspend fun downloadFile(
        url: String,
        fileName: String,
        accessToken: String
    ) {
        when (val result = fileRepository.saveFileLocal(
            url = url,
            fileName = fileName,
            token = accessToken
        )) {
            is ResultWork.Error -> {
                eventChannel.send(OnMessage(result.error.asUiText()))
            }

            is ResultWork.Success -> {}
        }
    }


    override fun onCleared() {
        super.onCleared()
        webSocketJob?.cancel()
        viewModelScope.launch {
            webSocketChatRepository.close()
        }
    }
}
