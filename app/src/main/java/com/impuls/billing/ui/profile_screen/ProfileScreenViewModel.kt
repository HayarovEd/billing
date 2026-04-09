package com.impuls.billing.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.repository.AuthRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
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
import kotlinx.coroutines.delay
import kotlin.time.Clock

class ProfileScreenViewModel(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state
        .onStart {
            _state.value.copy(
                isLoading = true,
            )
                .updateState()
            loadData(
                action = {
                    loadAccountData(it)
                },
                navigate = {
                    viewModelScope.launch {
                        eventChannel.send(ProfileScreenEvent.NavigateEvent)
                    }
                }
            )

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileScreenState()
        )

    private val eventChannel = Channel<ProfileScreenEvent>()
    val events = eventChannel.receiveAsFlow()


    fun onAction(action: ProfileScreenAction) {
        when (action) {
            is ProfileScreenAction.UpdatePhone -> {
                loadData(
                    action = {
                        updatePhone(
                            token = it,
                            phone = action.phone
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            is ProfileScreenAction.ConfirmEmail -> {
                loadData(
                    action = {
                        confirmValidateClientEmail(
                            token = it,
                            code = action.code
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )

            }

            is ProfileScreenAction.ConfirmPhone -> {
                loadData(
                    action = {
                        confirmValidateClientMobileNumber(
                            token = it,
                            code = action.code
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )

            }

            ProfileScreenAction.ResentCodeEmail -> {
                loadData(
                    action = {
                        timer()
                        validateEmail(it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )

            }

            ProfileScreenAction.ResentCodePhone -> {
                loadData(
                    action = {
                        timer()
                        validateMobilePhone(token = it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )

            }

            is ProfileScreenAction.UpdateEmail -> {
                loadData(
                    action = {
                        updateEmail(
                            token = it,
                            email = action.email
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )

            }
        }
    }


    private fun loadAccountData(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.getClientAccount(token)) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                    )
                        .updateState()
                    eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        account = result.data
                    )
                        .updateState()
                }
            }
        }
    }


    private fun updatePhone(
        token: String,
        phone: String
    ) {
        state.value.account?.let { account ->
            viewModelScope.launch {
                when (val result = accountRepository.setClientMobilePhone(
                    token = token,
                    uid = account.account.uid,
                    phone = phone
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData(token)
                        validateMobilePhone(token)
                        timer()
                    }
                }
            }
        }
    }

    private fun validateMobilePhone(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.validateClientMobileNumber(token)) {
                is ResultWork.Error -> {
                    eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        phoneRet = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private fun confirmValidateClientMobileNumber(
        token: String,
        code: String
    ) {
        state.value.phoneRet?.let {
            viewModelScope.launch {
                when (val result = accountRepository.confirmValidateClientMobileNumber(
                    token = token,
                    id = it,
                    code = code
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData(token)
                    }
                }
            }
        }

    }


    private fun updateEmail(
        token: String,
        email: String
    ) {
        state.value.account?.let { account ->
            viewModelScope.launch {
                when (val result = accountRepository.setClientEmail(
                    token = token,
                    uid = account.account.uid,
                    email = email
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData(token)
                        validateEmail(token)
                        timer()
                    }
                }
            }
        }
    }

    private fun validateEmail(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.validateClientEmail(token)) {
                is ResultWork.Error -> {
                    eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        emailRet = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private fun confirmValidateClientEmail(
        token: String,
        code: String
    ) {
        state.value.phoneRet?.let {
            viewModelScope.launch {
                when (val result = accountRepository.confirmValidateClientEmail(
                    token = token,
                    id = it,
                    code = code
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData(token)
                    }
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


    private fun loadData(
        action: (String) -> Unit,
        navigate: () -> Unit
    ) {
        viewModelScope.launch {
            dataStoreRepository.getBearer().collect { token ->
                if (token != null) {
                    val expired = jwtRepository.decodeJwt(token)?.expired
                    expired?.let { exp ->
                        if (exp < Clock.System.now().epochSeconds) {
                            dataStoreRepository.removeBearer()
                            navigate()
                        } else {
                            action(token)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        navigate()
                    }
                }
            }
        }
    }

    private fun ProfileScreenState.updateState() {
        _state.update {
            this
        }
    }
}