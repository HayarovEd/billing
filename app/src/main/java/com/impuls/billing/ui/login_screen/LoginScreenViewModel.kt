package com.impuls.billing.ui.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.model.LastAuthData
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.AuthRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val authRepository: AuthRepository,
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
            is LoginScreenAction.UpdateLogin -> {
                _state.value.copy(
                    login = action.login,
                )
                    .updateState()
            }

            is LoginScreenAction.UpdatePassword -> {
                _state.value.copy(
                    password = action.password,
                )
                    .updateState()
            }

            is LoginScreenAction.RecoverPassword -> {
                recoverPassword()
            }

            is LoginScreenAction.UpdateRecoverType -> {
                _state.value.copy(
                    type = action.recoverType,
                )
                    .updateState()
            }

            is LoginScreenAction.ConfirmWithCode -> {
                updatePasswordByCode(
                    code = action.code,
                    password = action.password
                )
            }
        }
    }

    private fun onLogin() {
        viewModelScope.launch {
            if (state.value.login.isBlank()) {
                eventChannel.send(LoginScreenEvent.OnMessage(DataError.ValidationError.EMAIL_BLANK.asUiText()))
            } else if (state.value.password.isBlank()) {
                eventChannel.send(LoginScreenEvent.OnMessage(DataError.ValidationError.PASSWORD_BLANK.asUiText()))
            } else {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                when (val result = authRepository.login(
                    username = state.value.login,
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
                        dataStoreRepository.saveBearer(result.data.token)
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                        eventChannel.send(LoginScreenEvent.NavigateEvent)
                        dataStoreRepository.setLocalAuthorization(
                            LastAuthData(
                                login = state.value.login,
                                password = state.value.password
                            )
                        )
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
                        login = result.data.login
                    )
                        .updateState()
                }
            }

        }
    }

    private fun recoverPassword() {
        viewModelScope.launch {
            timer()
            when (val result = authRepository.recoverPassword(
                login = state.value.login,
                transport = state.value.type.number
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(LoginScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        destination = result.data.destination
                    )
                        .updateState()
                    eventChannel.send(LoginScreenEvent.openConfirmDialog)
                }
            }
        }
    }

    private fun updatePasswordByCode(
        code: String,
        password: String
    ) {
        viewModelScope.launch {
            when (val result = authRepository.updatePasswordByCode(
                login = state.value.login,
                code = code,
                password = password
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(LoginScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    eventChannel.send(LoginScreenEvent.OnMessage(DataError.SuccessWork.SUCCESS_UPDATE_PASSWORD.asUiText()))
                }
            }
        }
    }

    private fun timer(count: Int = 10) {
        viewModelScope.launch {
            _state.value.copy(
                count = count
            )
                .updateState()
            for (i in count downTo 0) {
                _state.value.copy(
                    count = i
                )
                    .updateState()
                delay(1000)
            }
        }
    }

    private fun LoginScreenState.updateState() {
        _state.update {
            this
        }
    }
}