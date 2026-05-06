package com.impuls.billing.ui.settings_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.billing.domain.model.Notice
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asUiText
import com.impuls.billing.ui.uikit.model.NavigationRoute
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class SettingsScreenViewModel(
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val userId = savedStateHandle.toRoute<NavigationRoute.SettingsRoute>().userId

    private val _state = MutableStateFlow(SettingsScreenState())
    val state = _state
        .onStart {
            loadData(
                action = {
                    loadIntitalData(it)
                },
                navigate = {
                    viewModelScope.launch {
                        eventChannel.send(SettingsScreenEvent.NavigateEvent)
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsScreenState()
        )


    private val eventChannel = Channel<SettingsScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: SettingsScreenAction) {
        when (action) {
            is SettingsScreenAction.UpdateCheckEmail -> {
                _state.update { currentState ->
                    currentState.clientNotices?.let { client ->
                        val existingNotice = client.notices.find { it.type == action.typeId }
                        val updatedNotices = if (existingNotice != null) {
                            client.notices.map { notice ->
                                if (notice.type == action.typeId) notice.copy(email = action.isCheckEmail) else notice
                            }
                        } else {
                            client.notices + Notice(
                                descr = "",
                                email = action.isCheckEmail,
                                messenger = false,
                                name = "",
                                sms = false,
                                type = action.typeId,
                                value = "0"
                            )
                        }
                        currentState.copy(
                            clientNotices = client.copy(
                                notices = updatedNotices.filter { it.email || it.sms }
                            )
                        )
                    } ?: currentState
                }
            }

            is SettingsScreenAction.UpdateCheckSms -> {
                _state.update { currentState ->
                    currentState.clientNotices?.let { client ->
                        val existingNotice = client.notices.find { it.type == action.typeId }
                        val updatedNotices = if (existingNotice != null) {
                            client.notices.map { notice ->
                                if (notice.type == action.typeId) notice.copy(sms = action.isCheckSms) else notice
                            }
                        } else {
                            client.notices + Notice(
                                descr = "",
                                email = false,
                                messenger = false,
                                name = "",
                                sms = action.isCheckSms,
                                type = action.typeId,
                                value = "0"
                            )
                        }
                        currentState.copy(
                            clientNotices = client.copy(
                                notices = updatedNotices.filter { it.email || it.sms }
                            )
                        )
                    } ?: currentState
                }
            }

            SettingsScreenAction.UpdateNotices -> {
                loadData(
                    action = {
                        updateNotices(it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            is SettingsScreenAction.UpdateValue -> {
                _state.update { currentState ->
                    currentState.clientNotices?.let { client ->
                        val existingNotice = client.notices.find { it.type == action.typeId }
                        val updatedNotices = if (existingNotice != null) {
                            client.notices.map { notice ->
                                if (notice.type == action.typeId) notice.copy(value = action.value) else notice
                            }
                        } else {
                            client.notices + Notice(
                                descr = "",
                                email = false,
                                messenger = false,
                                name = "",
                                sms = false,
                                type = action.typeId,
                                value = action.value
                            )
                        }
                        currentState.copy(
                            clientNotices = client.copy(
                                notices = updatedNotices.filter { it.email || it.sms }
                            )
                        )
                    } ?: currentState
                }
            }

            is SettingsScreenAction.ConfirmEmail -> {
                loadData(
                    action = {
                        confirmValidateClientEmail(
                            token = it,
                            code = action.code
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            is SettingsScreenAction.ConfirmPhone -> {
                loadData(
                    action = {
                        confirmValidateClientMobileNumber(
                            token = it,
                            code = action.code
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            SettingsScreenAction.ResentCodeEmail -> {
                loadData(
                    action = {
                        timer()
                        validateEmail(it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            SettingsScreenAction.ResentCodePhone -> {
                loadData(
                    action = {
                        timer()
                        validateMobilePhone(it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            is SettingsScreenAction.UpdateEmail -> {
                loadData(
                    action = {
                        updateEmail(
                            token = it,
                            email = action.email
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            is SettingsScreenAction.UpdatePhone -> {
                loadData(
                    action = {
                        updatePhone(
                            token = it,
                            phone = action.phone
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            is SettingsScreenAction.UpdatePassword -> {
                loadData(
                    action = {
                        updatePassword(
                            token = it,
                            new = action.new,
                            old = action.old,
                            confirm = action.confirm
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }

            SettingsScreenAction.LogOff -> {
                loadData(
                    action = {
                        logOff(it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(SettingsScreenEvent.NavigateEvent)
                        }
                    }
                )
            }
        }
    }


    private fun loadIntitalData(token: String) {
        viewModelScope.launch {
            _state.value.copy(
                isLoading = true,
            )
                .updateState()
            val differdClientInfo = async {
                accountRepository.getClientInfo(
                    token = token,
                )
            }
            val differdClientNotices = async {
                accountRepository.getClientNotices(
                    token = token,
                    userid = userId
                )
            }
            val resultClientInfo = differdClientInfo.await()
            val resultClientNotices = differdClientNotices.await()
            when (resultClientInfo) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(resultClientInfo.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        clientInfo = resultClientInfo.data,
                    )
                        .updateState()
                }
            }
            when (resultClientNotices) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(resultClientNotices.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        clientNotices = resultClientNotices.data,
                    )
                        .updateState()
                }
            }
            _state.value.copy(
                isLoading = false,
            )
                .updateState()
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

    private fun updateNotices(token: String) {
        viewModelScope.launch {
            _state.value.clientNotices?.let { cNotices ->
                when (val result =
                    accountRepository.updateClientNotices(
                        token = token,
                        clientNotices = cNotices
                    )) {
                    is ResultWork.Error -> {
                        eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        eventChannel.send(SettingsScreenEvent.OnMessage(DataError.SuccessWork.SUCCESS_UPDATE_NOTICES.asUiText()))
                    }
                }
            }

        }
    }

    private fun updatePhone(
        token: String,
        phone: String
    ) {
        viewModelScope.launch {
            when (val result = accountRepository.setClientMobilePhone(
                token = token,
                uid = userId,
                phone = phone
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    validateMobilePhone(token)
                    timer()
                    _state.value.copy(
                        clientInfo = state.value.clientInfo?.copy(mobile = phone)
                    )
                        .updateState()
                }
            }
        }
    }

    private fun validateMobilePhone(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.validateClientMobileNumber(token)) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
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
                        eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadIntitalData(token)
                    }
                }
            }
        }

    }


    private fun updateEmail(
        token: String,
        email: String
    ) {
        viewModelScope.launch {
            when (val result = accountRepository.setClientEmail(
                token = token,
                uid = userId,
                email = email
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    //loadIntitalData(token)
                    validateEmail(token)
                    timer()
                    _state.value.copy(
                        clientInfo = state.value.clientInfo?.copy(email = email)
                    )
                        .updateState()
                }
            }
        }
    }

    private fun validateEmail(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.validateClientEmail(token)) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
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
        state.value.emailRet?.let {
            viewModelScope.launch {
                when (val result = accountRepository.confirmValidateClientEmail(
                    token = token,
                    id = it,
                    code = code
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadIntitalData(token)
                    }
                }
            }
        }

    }

    private fun updatePassword(
        token: String,
        old: String,
        new: String,
        confirm: String
    ) {
        _state.value.clientInfo?.let { info ->
            viewModelScope.launch {
                _state.value.copy(
                    isUpdatingPassword = true
                )
                    .updateState()
                if (old != info.password) {
                    eventChannel.send(SettingsScreenEvent.OnMessage(DataError.ValidationError.OLD_PASSWORD_INCORRECT.asUiText()))
                    _state.value.copy(
                        isUpdatingPassword = false
                    )
                        .updateState()
                    return@launch
                }
                if (new != confirm) {
                    eventChannel.send(SettingsScreenEvent.OnMessage(DataError.ValidationError.PASSWORDS_NOT_EQUALS.asUiText()))
                    _state.value.copy(
                        isUpdatingPassword = false
                    )
                        .updateState()
                    return@launch
                }
                when (val result = accountRepository.updatePassword(
                    token = token,
                    oldPassword = old,
                    newPassword = new
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        loadIntitalData(token)
                        eventChannel.send(SettingsScreenEvent.OnMessage(DataError.SuccessWork.SUCCESS_UPDATE_PASSWORD.asUiText()))
                    }
                }
                _state.value.copy(
                    isUpdatingPassword = false
                )
                    .updateState()
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

    private fun logOff(token: String) {
        _state.value.copy(
            isLoading = true
        )
            .updateState()
        viewModelScope.launch {
            when (val result = accountRepository.logOut(token)) {
                is ResultWork.Error -> {
                    eventChannel.send(SettingsScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    if (result.data) {
                        dataStoreRepository.removeBearer()
                        eventChannel.send(SettingsScreenEvent.NavigateEvent)
                    } else {
                        eventChannel.send(SettingsScreenEvent.OnMessage(DataError.Network.UNKNOWN.asUiText()))
                    }
                }
            }

        }
    }

    private fun SettingsScreenState.updateState() {
        _state.update {
            this
        }
    }
}