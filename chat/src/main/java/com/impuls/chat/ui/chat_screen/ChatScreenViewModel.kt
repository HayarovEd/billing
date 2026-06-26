package com.impuls.chat.ui.chat_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.WebSocketChatMessage
import com.impuls.chat.domain.repository.AuthUseCase
import com.impuls.chat.domain.repository.NotificationRepository
import com.impuls.chat.domain.repository.RemoteChatRepository
import com.impuls.chat.domain.repository.WebSocketChatRepository
import com.impuls.chat.domain.utils.ResultWork
import com.impuls.chat.domain.utils.Utils
import com.impuls.chat.ui.navigation.NavigationChatRoute
import com.impuls.chat.ui.uikit.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
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
    private val notificationRepository: NotificationRepository
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

    private val eventChannel = Channel<ChatScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ChatScreenAction) {
        when (action) {
            is ChatScreenAction.SendMessage -> {
                loadData {
                    sendMessage(
                        accessToken = it,
                        message = action.message
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
                    eventChannel.send(ChatScreenEvent.OnMessage(it.asUiText()))
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
                    eventChannel.send(ChatScreenEvent.OnMessage(result.error.asUiText()))
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
        message: String
    ) {
        viewModelScope.launch {
            val newMessage = Message(
                date = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                id = "-1",
                isSupport = false,
                text = message,
                statusMessage = StatusMessage.SENDED
            )
            _state.update {
                it.copy(
                    currentMessage = "",
                    tmpMessage = newMessage
                )
            }
            when (val result = remoteRepository.sendMessage(
                accessToken = accessToken,
                chatId = routeData.chatId,
                message = message
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(ChatScreenEvent.OnMessage(result.error.asUiText()))
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
        }
    }

    private fun updateChat(accessToken: String) {
        viewModelScope.launch {
            webSocketChatRepository.updateLiveChat(accessToken =accessToken, chatId = routeData.chatId).collect { result ->
                when (result) {
                    is ResultWork.Error -> {
                        eventChannel.send(ChatScreenEvent.OnMessage(result.error.asUiText()))
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
                                    title = data.chat.subgroup.name,
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            webSocketChatRepository.close()
        }
    }
}
