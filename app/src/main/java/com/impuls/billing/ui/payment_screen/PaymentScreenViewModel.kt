package com.impuls.billing.ui.payment_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.billing.domain.repository.PaymentRepository
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asTextMessage
import com.impuls.billing.ui.uikit.asUiText
import com.impuls.billing.ui.uikit.model.NavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

class PaymentScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val paymentRepository: PaymentRepository,
) : ViewModel() {


    private val _state = MutableStateFlow(
        PaymentScreenState(
            number = savedStateHandle.toRoute<NavigationRoute.Payment>().number,
        )
    )
    val state = _state
        .onStart {
            loadInitialData()
            checkLs()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PaymentScreenState()
        )

    private val eventChannel = Channel<PaymentScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PaymentScreenAction) {
        when (action) {
            is PaymentScreenAction.UpdateAmount -> {
                _state.value.copy(
                    amountString = action.amount,
                )
                    .updateState()
            }

            is PaymentScreenAction.UpdatePaymentScreenType -> {
                _state.value.copy(
                    paymentScreenType = action.paymentScreenType,
                )
                    .updateState()
            }

            is PaymentScreenAction.UpdateMethodPayment -> {
                _state.value.copy(
                    chosenMethod = action.typePayment,
                )
                    .updateState()
            }

            PaymentScreenAction.Payment -> {
                toPay()
            }

            is PaymentScreenAction.UpdatePhone -> {
                _state.value.copy(
                    paymentPhone = action.phone,
                )
                    .updateState()
            }

            PaymentScreenAction.CheckPayment -> {
                checkPayment()
            }

            PaymentScreenAction.SendSpbLink -> {
                state.value.paymentSbp?.let { sbp ->
                    viewModelScope.launch {
                        eventChannel.send(PaymentScreenEvent.SendLink(sbp.wayToPay.url))
                    }
                }
            }
        }
    }

    private fun loadInitialData() {
        _state.value.copy(
            isLoading = true,
        )
            .updateState()
        viewModelScope.launch {
            when (val result = paymentRepository.loadPaymentData()) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                    )
                        .updateState()
                    eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        companyPayment = result.data
                    )
                        .updateState()
                    loadImage(
                        category = result.data.serviceType,
                        service = result.data.serviceCode
                    )
                }
            }
        }
        viewModelScope.launch {
            when (val result = paymentRepository.loadToken()) {
                is ResultWork.Error -> {
                    eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        paymentToken = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private fun loadImage(
        category: String,
        service: String
    ) {
        viewModelScope.launch {
            when (val result = paymentRepository.loadLogoUrl(
                category = category,
                service = service
            )) {
                is ResultWork.Error -> {
                    eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        imageData = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private fun checkLs() {

        viewModelScope.launch {
            delay(300)
            state.value.number?.let { nmb ->
                when (val result = paymentRepository.checkAccount(
                    number = nmb
                )) {
                    is ResultWork.Error -> {
                        eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            resultCheck = result.data
                        )
                            .updateState()
                    }
                }
            }
        }
    }


    private fun toPay() {
        state.value.chosenMethod?.let { method ->
            when (method) {
                TypePayment.CARD -> {
                    requestCardPay()
                }

                TypePayment.SBER_PAY -> {
                    if (state.value.paymentPhone.isBlank()) {
                        _state.value.copy(
                            paymentScreenType = PaymentScreenType.ENTER_PHONE,
                        )
                            .updateState()
                    } else {
                        sendPush()
                    }
                }

                TypePayment.SBP -> {
                    requestSbpPay()
                }
            }
        }
    }

    private fun requestCardPay() {
        state.value.companyPayment?.let { company ->
            viewModelScope.launch {
                state.value.amountNull?.let { amount ->
                    _state.value.copy(
                        isLoading = true,
                        paymentSber = null,
                    )
                        .updateState()
                    state.value.number?.let {
                        when (val result = paymentRepository.payCard(
                            payCurrency = company.currency,
                            amount = amount,
                            ledgerAccount = it
                        )) {
                            is ResultWork.Error -> {
                                _state.value.copy(
                                    isLoading = false,
                                )
                                    .updateState()
                                eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                            }

                            is ResultWork.Success -> {
                                _state.value.copy(
                                    isLoading = false,
                                    paymentScreenType = PaymentScreenType.SBER_PAY,
                                    paymentCard = result.data
                                )
                                    .updateState()
                            }
                        }
                    }

                }

            }
        }
    }

    private fun sendPush() {
        state.value.companyPayment?.let { company ->
            viewModelScope.launch {
                state.value.amountNull?.let { amount ->
                    _state.value.copy(
                        isLoading = true,
                        paymentCard = null,
                    )
                        .updateState()
                    state.value.number?.let {
                        when (val result = paymentRepository.paySber(
                            payCurrency = company.currency,
                            amount = amount,
                            phone = state.value.paymentPhone,
                            ledgerAccount = it
                        )) {
                            is ResultWork.Error -> {
                                _state.value.copy(
                                    isLoading = false,
                                )
                                    .updateState()
                                eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                            }

                            is ResultWork.Success -> {
                                _state.value.copy(
                                    isLoading = false,
                                    paymentScreenType = PaymentScreenType.SBER_PAY,
                                    paymentSber = result.data
                                )
                                    .updateState()
                            }
                        }
                    }

                }

            }
        }
    }

    private fun requestSbpPay() {
        state.value.companyPayment?.let { company ->
            viewModelScope.launch {
                state.value.amountNull?.let { amount ->
                    _state.value.copy(
                        isLoading = true,
                        paymentCard = null,
                    )
                        .updateState()
                    state.value.number?.let {
                        when (val result = paymentRepository.paySbp(
                            payCurrency = company.currency,
                            amount = amount,
                            ledgerAccount = it
                        )) {
                            is ResultWork.Error -> {
                                _state.value.copy(
                                    isLoading = false,
                                )
                                    .updateState()
                                eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                            }

                            is ResultWork.Success -> {
                                _state.value.copy(
                                    isLoading = false,
                                    paymentScreenType = PaymentScreenType.SBP,
                                    paymentSbp = result.data
                                )
                                    .updateState()
                            }
                        }
                    }

                }

            }
        }
    }

    private fun checkPayment() {
        viewModelScope.launch {
            when (state.value.paymentScreenType) {
                PaymentScreenType.CARD -> {}
                PaymentScreenType.SBER_PAY -> {
                    state.value.paymentSber?.let { sber ->
                        when (val result =
                            paymentRepository.checkStatusPayment(sber.receipt.regPayNum)) {
                            is ResultWork.Error -> {
                                eventChannel.send(PaymentScreenEvent.OnMessage(result.error.asUiText()))
                            }

                            is ResultWork.Success -> {
                                eventChannel.send(PaymentScreenEvent.OnMessage(result.data.state.asTextMessage()))
                            }
                        }
                    }
                }

                PaymentScreenType.SBP -> {}
                else -> {}
            }
        }
    }


    private fun PaymentScreenState.updateState() {
        _state.update {
            this
        }
    }
}