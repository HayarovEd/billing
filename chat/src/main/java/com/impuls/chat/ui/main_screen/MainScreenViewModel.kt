package com.impuls.chat.ui.main_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.chat.domain.repository.AuthUseCase
import com.impuls.chat.domain.repository.RemoteChatRepository
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
import kotlin.time.Duration.Companion.milliseconds

class MainScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val remoteRepository: RemoteChatRepository,
    private val authUseCase: AuthUseCase
) : ViewModel() {


    private val routeData = savedStateHandle.toRoute<NavigationChatRoute.MainRoute>()

    private val _state = MutableStateFlow(
        MainScreenState(
            name = routeData.name,
            login = routeData.login,
            password = routeData.password,
            email = routeData.email,
            operId = routeData.operId
        )
    )
    val state = _state
        .onStart {
            loadData(
                action = { token ->
                    coroutineScope {
                        launch { getThemes(token) }
                        launch { getUserChats(token) }
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    private val eventChannel = Channel<MainScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.UpdateEmail -> {
                _state.update {
                    it.copy(
                        email = action.email
                    )
                }
            }
            is MainScreenAction.UpdateMessage -> {
                _state.update {
                    it.copy(
                        message = action.message
                    )
                }
            }
            is MainScreenAction.UpdateName -> {
                _state.update {
                    it.copy(
                        name = action.name
                    )
                }
            }
            is MainScreenAction.UpdatePhone -> {
                _state.update {
                    it.copy(
                        phone = action.phone
                    )
                }
            }
            is MainScreenAction.UpdateTheme -> {
                _state.update {
                    it.copy(
                        selectedTheme = action.theme
                    )
                }
            }
            MainScreenAction.CreateChat -> {
                loadData {
                    createChat(it)
                }
            }
            is MainScreenAction.OnChatClick -> {
                viewModelScope.launch {
                    eventChannel.send(MainScreenEvent.NavigateEvent(action.chatId))
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
                    eventChannel.send(MainScreenEvent.OnMessage(it.asUiText()))
                },
                name = currentState.name,
                username = currentState.login,
                email = currentState.email,
                password = currentState.password,
            )
            _state.update { it.copy(isLoading = false) }
        }
    }


    private suspend fun getThemes(accessToken: String) {
        _state.value.operId?.let { oi->
            when (val result = remoteRepository.getGroupByOperId(
                accessToken = accessToken,
                operId = oi
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(MainScreenEvent.OnMessage(result.error.asUiText()))
                }
                is ResultWork.Success -> {
                    _state.update {
                        it.copy(
                            group = result.data
                        )
                    }
                }
            }
        }
    }

    private suspend fun getUserChats(accessToken: String) {
        when (val result = remoteRepository.getOpenChatsByUser(
            accessToken = accessToken,
        )) {
            is ResultWork.Error -> {
                eventChannel.send(MainScreenEvent.OnMessage(result.error.asUiText()))
            }
            is ResultWork.Success -> {
                _state.update {
                    it.copy(
                        userChats = result.data
                    )
                }
            }
        }
    }

    private suspend fun createChat(accessToken: String) {
        if (_state.value.selectedTheme == null) {
            eventChannel.send(MainScreenEvent.MessageEvent(MessageErrorEvent.SELECTED_THEME_NULL))
            return
        }
        if (_state.value.message.isEmpty()) {
            eventChannel.send(MainScreenEvent.MessageEvent(MessageErrorEvent.MESSAGE_EMPTY))
            return
        }
        if (_state.value.phone.isEmpty()) {
            eventChannel.send(MainScreenEvent.MessageEvent(MessageErrorEvent.PHONE_EMPTY))
            return
        }
        if (_state.value.name.isEmpty()) {
            eventChannel.send(MainScreenEvent.MessageEvent(MessageErrorEvent.NAME_EMPTY))
            return
        }
        if (!Utils.isValidEmail(_state.value.email)) {
            eventChannel.send(MainScreenEvent.MessageEvent(MessageErrorEvent.WRONG_EMAIL))
            return
        }
        _state.value.selectedTheme?.let {
            when (val result = remoteRepository.createChat(
                accessToken = accessToken,
                groupId = it.id,
                name = _state.value.name,
                phone = _state.value.phone,
                message = _state.value.message,
                email = _state.value.email
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(MainScreenEvent.OnMessage(result.error.asUiText()))
                }
                is ResultWork.Success -> {
                    _state.update { st ->
                        st.copy(
                            chat = result.data
                        )
                    }
                    eventChannel.send(MainScreenEvent.NavigateEvent(result.data.id))
                }
            }
        }
    }
}