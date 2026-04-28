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

    private fun SettingsScreenState.updateState() {
        _state.update {
            this
        }
    }
}