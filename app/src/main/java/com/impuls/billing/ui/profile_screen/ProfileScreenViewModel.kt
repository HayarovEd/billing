package com.impuls.billing.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.RemoteRepository
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

class ProfileScreenViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {


    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state
        .onStart {
            _state.value.copy(
                isLoading = true,
            )
                .updateState()
            loadAccountData()
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
                updatePhone(action.phone)
            }

            is ProfileScreenAction.ConfirmEmail -> {
                confirmValidateClientEmail(action.code)
            }

            is ProfileScreenAction.ConfirmPhone -> {
                confirmValidateClientMobileNumber(action.code)
            }

            ProfileScreenAction.ResentCodeEmail -> {
                timer()
                validateEmail()
            }

            ProfileScreenAction.ResentCodePhone -> {
                timer()
                validateMobilePhone()
            }

            is ProfileScreenAction.UpdateEmail -> {
                updateEmail(action.email)
            }
        }
    }


    private fun loadAccountData() {
        viewModelScope.launch {
            when (val result = remoteRepository.getClientAccount()) {
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


    private fun updatePhone(phone: String) {
        state.value.account?.let { account ->
            viewModelScope.launch {
                when (val result = remoteRepository.setClientMobilePhone(
                    uid = account.account.uid,
                    phone = phone
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData()
                        validateMobilePhone()
                        timer()
                    }
                }
            }
        }
    }

    private fun validateMobilePhone() {
        viewModelScope.launch {
            when (val result = remoteRepository.validateClientMobileNumber()) {
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

    private fun confirmValidateClientMobileNumber(code: String) {
        state.value.phoneRet?.let {
            viewModelScope.launch {
                when (val result = remoteRepository.confirmValidateClientMobileNumber(
                    id = it,
                    code = code
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData()
                    }
                }
            }
        }

    }


    private fun updateEmail(email: String) {
        state.value.account?.let { account ->
            viewModelScope.launch {
                when (val result = remoteRepository.setClientEmail(
                    uid = account.account.uid,
                    email = email
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData()
                        validateEmail()
                        timer()
                    }
                }
            }
        }
    }

    private fun validateEmail() {
        viewModelScope.launch {
            when (val result = remoteRepository.validateClientEmail()) {
                is ResultWork.Error -> {
                   // eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
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

    private fun confirmValidateClientEmail(code: String) {
        state.value.phoneRet?.let {
            viewModelScope.launch {
                when (val result = remoteRepository.confirmValidateClientEmail(
                    id = it,
                    code = code
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadAccountData()
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

    private fun ProfileScreenState.updateState() {
        _state.update {
            this
        }
    }
}