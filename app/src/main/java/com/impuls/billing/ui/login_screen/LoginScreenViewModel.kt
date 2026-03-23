package com.impuls.billing.ui.login_screen

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.RemoteRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel

class LoginScreenViewModel(
    private val remoteRepository: RemoteRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state
        .onStart {
            setLocalAuthorization()
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
            LoginScreenAction.OnLogin -> onLogin()
            is LoginScreenAction.UpdateEmail -> {
                _state.value.copy(
                    email = action.email,
                )
                    .updateState()
            }

            is LoginScreenAction.UpdatePassword -> {
                _state.value.copy(
                    password = action.password,
                )
                    .updateState()
            }
        }
    }

    private fun onLogin() {
        viewModelScope.launch {
            if (state.value.email.isBlank()) {
                eventChannel.send(LoginScreenEvent.OnMessage(DataError.ValidationError.EMAIL_BLANK.asUiText()))
            } else if (Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches()) {
                eventChannel.send(LoginScreenEvent.OnMessage(DataError.ValidationError.EMAIL_NOT_VALID.asUiText()))
            } else if (state.value.password.isBlank()) {
                eventChannel.send(LoginScreenEvent.OnMessage(DataError.ValidationError.PASSWORD_BLANK.asUiText()))
            } else {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                when (val result = remoteRepository.login(
                    username = state.value.email,
                    password = state.value.password
                )) {
                    is ResultWork.Error -> {
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                        eventChannel.send(LoginScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                        eventChannel.send(LoginScreenEvent.NavigateEvent)
                    }
                }
            }
        }
    }

    private fun setLocalAuthorization() {
        viewModelScope.launch {
            when (val result = dataStoreRepository.readStateLocalAuthorization()) {
                is ResultWork.Error -> {
                    eventChannel.send(LoginScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        password = result.data.password,
                        email = result.data.email
                    )
                        .updateState()
                }
            }

        }
    }

    private fun LoginScreenState.updateState() {
        _state.update {
            this
        }
    }
}