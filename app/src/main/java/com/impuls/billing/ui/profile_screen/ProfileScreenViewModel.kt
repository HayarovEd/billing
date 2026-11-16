package com.impuls.billing.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.HomeRepository
import com.impuls.billing.domain.repository.JwtRepository
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asUiText
import com.impuls.chat.domain.repository.DataStoreChatRepository
import com.impuls.chat.service.PushMessageData
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

class ProfileScreenViewModel(
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
    private val homeRepository: HomeRepository,
    private val dataStoreChatRepository: DataStoreChatRepository
) : ViewModel() {


    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state
        .onStart {
            _state.value.copy(
                isLoading = true,
            )
                .updateState()
            viewModelScope.launch {
                loadData(
                    action = {
                        loadAccountData(it)
                        loadSharePosts(it)
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(ProfileScreenEvent.NavigateEvent)
                        }
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileScreenState()
        )

    private val eventChannel = Channel<ProfileScreenEvent>()
    val events = eventChannel.receiveAsFlow()


    fun onAction(action: ProfileScreenAction) {
        viewModelScope.launch {
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

                is ProfileScreenAction.FcmMessage -> {
                    handleIntent(action.pushMessageData)
                }
            }
        }
    }


    private suspend fun loadAccountData(token: String) {
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
                    account = result.data
                )
                    .updateState()
                dataStoreChatRepository.saveUserBillingData(
                    name = result.data.account.name,
                    login = result.data.account.login,
                    password = result.data.account.password,
                    email = result.data.account.email,
                    billingToken = _state.value.token
                )
                if (result.data.agreements.isNotEmpty()) {
                    loadVGroups(
                        token = token,
                        agrmId = result.data.agreements.first().agrmId
                    )
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
                delay(1000.milliseconds)
            }
        }
    }


    private suspend fun loadData(
        action: suspend (String) -> Unit,
        navigate: () -> Unit
    ) {
        val token = dataStoreRepository.getBearer().firstOrNull()
        if (token != null) {
            val expired = jwtRepository.decodeJwt(token)?.expired
            expired?.let { exp ->
                if (exp < Clock.System.now().epochSeconds) {
                    dataStoreRepository.removeBearer()
                    navigate()
                } else {
                    _state.update { it.copy(token = token) }
                    action(token)
                }
            }
        } else {
            navigate()
        }
    }


    private suspend fun loadVGroups(
        token: String,
        agrmId: Long
    ) {
        when (val result = accountRepository.getVGroups(
            token = token,
            agrmId = agrmId
        )) {
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
                    vGroups = result.data,
                )
                    .updateState()
                if (result.data.isNotEmpty()) {
                    coroutineScope {
                        val deferredUsbBoxes = async {
                            loadUsbBoxes(
                                token = token,
                                vgId = result.data.first().vgId
                            )
                        }
                        val deferredVgroupPassword = async {
                            loadVgroupPassword(
                                token = token,
                                vgId = result.data.first().vgId
                            )
                        }
                        deferredUsbBoxes.await()
                        deferredVgroupPassword.await()
                    }
                }
            }
        }
    }

    private suspend fun loadUsbBoxes(
        token: String,
        vgId: Long
    ) {
        when (val result = homeRepository.getUsbBoxByVgroup(
            token = token,
            vgId = vgId
        )) {
            is ResultWork.Error -> {
                eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    vGroupUsbBoxes = result.data,
                )
                    .updateState()
            }
        }
    }

    private suspend fun loadVgroupPassword(
        token: String,
        vgId: Long
    ) {
        when (val result = accountRepository.getClientVgroupPassword(
            token = token,
            vgId = vgId
        )) {
            is ResultWork.Error -> {
                eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    vGroupPassword = result.data,
                )
                    .updateState()
            }
        }
    }

    private suspend fun loadSharePosts(token: String) {
        when (val result = accountRepository.getClientSharedPosts(token = token)) {
            is ResultWork.Error -> {
                eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    sharedPosts = result.data,
                )
                    .updateState()
            }
        }
    }

    fun handleIntent(pushMessageData: PushMessageData) {
        viewModelScope.launch {
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
            _state.value.copy(
                isLoading = false,
            )
                .updateState()
            pushMessageData.chatId?.let { chatId ->
                eventChannel.send(
                    ProfileScreenEvent.NavigateChat(
                        chatId = chatId,
                    )
                )
            }
        }
    }

    private fun ProfileScreenState.updateState() {
        _state.update {
            this
        }
    }
}