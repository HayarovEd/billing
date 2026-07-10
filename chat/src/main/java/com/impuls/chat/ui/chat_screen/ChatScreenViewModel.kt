package com.impuls.chat.ui.chat_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.chat.domain.model.DownloadTypeFromUseCase
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.model.StatusMessage
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
import com.impuls.chat.ui.chat_screen.ChatScreenEvent.*
import com.impuls.chat.ui.navigation.NavigationChatRoute
import com.impuls.chat.ui.uikit.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

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
        )
    )
    val state = _state
        .onStart {
            loadData { token ->
                getChatById(token)
            }
            loadData { token ->
                updateChat(token)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatScreenState()
        )

    init {
        getToken()
    }

    private val eventChannel = Channel<ChatScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ChatScreenAction) {
        when (action) {
            is ChatScreenAction.SendMessage -> {
                loadData {
                    sendMessage(
                        accessToken = it,
                        message = action.message,
                        uriFiles = _state.value.attachedFiles
                    )
                }

            }

            ChatScreenAction.CloseChat -> {
                loadData {
                    closeChat(it)
                }
            }

            is ChatScreenAction.UpdateCurrentMessage -> {
                _state.update {
                    it.copy(
                        currentMessage = action.message
                    )
                }
            }

            is ChatScreenAction.AttachFile -> {
                _state.update {
                    it.copy(
                        attachedFiles = it.attachedFiles + action.uri
                    )
                }
            }

            is ChatScreenAction.RemoveFile -> {
                _state.update {
                    it.copy(
                        attachedFiles = it.attachedFiles - action.uri
                    )
                }
            }

            is ChatScreenAction.SetMessageAsRead -> {
                loadData {
                    markMethodAsRead(
                        messageId = action.messageId,
                        accessToken = it
                    )
                }
            }

            is ChatScreenAction.ViewMedia -> {
                _state.update {
                    it.copy(
                        selectedFile = action.file
                    )
                }
            }

            is ChatScreenAction.OpenFileAnotherApp -> {
                loadData {
                    openFile(
                        fileName = action.fileName,
                        originalName = action.originalName,
                        mimeType = action.mimeType,
                        accessToken = it
                    )
                }
            }

            is ChatScreenAction.SaveFileLocal -> {
                loadData {
                    downloadFile(
                        url = action.fileName,
                        fileName = action.originalName,
                        accessToken = it
                    )
                }
            }
        }
    }


    private fun getToken() {
        viewModelScope.launch {
            dataStoreRepository.getToken().collect { collector ->
                _state.update {
                    it.copy(
                        token = collector?.access ?: ""
                    )
                }
            }
        }
    }

    private fun loadData(
        action: suspend (String) -> Unit,
    ) {
        viewModelScope.launch {
            delay(500.milliseconds)
            val currentState = _state.value
            _state.update { it.copy(isLoading = true) }
            authUseCase.invoke(
                function = {
                    action(it)
                },
                onError = {
                    eventChannel.send(OnMessage(it.asUiText()))
                },
                name = currentState.name,
                username = currentState.login,
                email = currentState.email,
                password = currentState.password,
            )
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun getChatById(accessToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = remoteRepository.getChatById(
                accessToken = accessToken,
                chatId = routeData.chatId
            )) {
                is ResultWork.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            chat = result.data
                        )
                    }
                }
            }
        }
    }

    private fun sendMessage(
        accessToken: String,
        message: String,
        uriFiles: List<android.net.Uri>
    ) {
        viewModelScope.launch {
            val newMessage = Message(
                date = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                id = "-1",
                isSupport = false,
                text = message,
                statusMessage = StatusMessage.SENDED,
                files = emptyList() //TODO
            )
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
    }

    private fun closeChat(accessToken: String) {
        viewModelScope.launch {
            eventChannel.send(NavigateToLogin)
        }

        /*viewModelScope.launch {
            when (val result = remoteRepository.closeChat(
                accessToken = accessToken,
                chatId = routeData.chatId
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(ChatScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    webSocketChatRepository.close()
                    eventChannel.send(ChatScreenEvent.NavigateToLogin)
                }
            }
        }*/
    }

    private fun updateChat(accessToken: String) {
        viewModelScope.launch {
            webSocketChatRepository.updateLiveChat(
                accessToken = accessToken,
                chatId = routeData.chatId
            ).collect { result ->
                when (result) {
                    is ResultWork.Error -> {
                        eventChannel.send(OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        val data = result.data
                        if (data is WebSocketChatMessage.UpdateChatMessage) {
                            var newMsgs = listOf<Message>()
                            _state.update { state ->
                                val mergeResult = state.chat?.let { existingChat ->
                                    Utils.mergeChatUpdates(existingChat, data.chat)
                                } ?: (data.chat to emptyList())

                                newMsgs = mergeResult.second

                                state.copy(
                                    tmpMessage = null,
                                    chat = mergeResult.first
                                )
                            }

                            newMsgs.forEach { message ->
                                notificationRepository.showNotification(
                                    title = data.chat.group.name,
                                    message = message.text,
                                    chatId = data.chat.id
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun markMethodAsRead(
        messageId: String,
        accessToken: String
    ) {
        viewModelScope.launch {
            when (val result = remoteRepository.markMessageAsRead(
                accessToken = accessToken,
                messageId = messageId
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {}
            }
        }
    }


    private fun openFile(
        fileName: String,
        mimeType: MimeTypes.MimeType,
        accessToken: String,
        originalName: String
    ) {
        viewModelScope.launch {
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
    }

    private fun  downloadFile(
        url: String,
        fileName: String,
        accessToken: String
    ) {
        viewModelScope.launch {
            when (val result = fileRepository.saveFileLocal(
                url = url,
                fileName = fileName,
                token = accessToken
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> { }
            }
        }
    }


    override fun onCleared() {
        viewModelScope.launch {
            webSocketChatRepository.close()
        }
    }
}
