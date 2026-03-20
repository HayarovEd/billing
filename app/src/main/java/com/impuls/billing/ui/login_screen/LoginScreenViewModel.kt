package com.impuls.billing.ui.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel

class LoginScreenViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {


    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state
        .onStart {
           onLogin()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginScreenState()
        )

    private val eventChannel = Channel<LoginScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginScreenAction) {
        when (action) {
            else -> {
                // TODO("Handle actions")
                /*viewModelScope.launch {
                    eventChannel.send(
                        //TODO
                    )
                }*/
            }
        }
    }

    private fun onLogin() {
        viewModelScope.launch {
            remoteRepository.login(
                username = "pycutbvk45",
                password = "342py23854"
            )
        }
    }

    private fun LoginScreenState.updateState() {
        _state.update {
            this
        }
    }
}