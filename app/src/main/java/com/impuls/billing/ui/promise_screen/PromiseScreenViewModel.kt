package com.impuls.billing.ui.promise_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.billing.domain.repository.BillingPaymentRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
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

class PromiseScreenViewModel(
    private val billingPaymentRepository: BillingPaymentRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val agrmId = savedStateHandle.toRoute<NavigationRoute.PromiseRoute>().agrmId

    private val _state = MutableStateFlow(PromiseScreenState())
    val state = _state
        .onStart {
            loadData(
                action = {
                    loadInitialData(it)
                },
                navigate = {
                    viewModelScope.launch {
                        eventChannel.send(PromiseScreenEvent.NavigateEvent)
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PromiseScreenState()
        )

    private val eventChannel = Channel<PromiseScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PromiseScreenAction) {
        when (action) {
            is PromiseScreenAction.UpdateAmount -> updateAmount(action.amount)
        }
    }

    private fun loadInitialData(token: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    resultUiState = ResultUiState.Loading,
                )
            }
            val ppsSettingsDeferred = async {
                billingPaymentRepository.getPPSettings(
                    token = token,
                    agrmId = agrmId
                )
            }
            val paymentDeferred = async {
                billingPaymentRepository.getRecommendedPayment(
                    token = token,
                    agrmId = agrmId
                )
            }

            val ppsSettingsResult = ppsSettingsDeferred.await()
            val paymentResult = paymentDeferred.await()

            when {
                ppsSettingsResult is ResultWork.Success && paymentResult is ResultWork.Success -> {
                    _state.update {
                        it.copy(
                            resultUiState = ResultUiState.Success(
                                promisePayment = paymentResult.data,
                                ppSettings = ppsSettingsResult.data
                            )
                        )
                    }

                }

                else -> {
                    when {
                        ppsSettingsResult is ResultWork.Error -> {
                            _state.update {
                                it.copy(
                                    resultUiState = ResultUiState.Error(message = ppsSettingsResult.error.asUiText())
                                )
                            }

                            eventChannel.send(PromiseScreenEvent.OnMessage(message = ppsSettingsResult.error.asUiText()))
                        }

                        paymentResult is ResultWork.Error -> {
                            _state.update {
                                it.copy(
                                    resultUiState = ResultUiState.Error(message = paymentResult.error.asUiText())
                                )
                            }

                            eventChannel.send(PromiseScreenEvent.OnMessage(message = paymentResult.error.asUiText()))
                        }
                    }
                }
            }
        }
    }

    private fun updateAmount(amount: String) {
        val success = (_state.value.resultUiState as ResultUiState.Success)
        success.ppSettings?.let { pps ->
            _state.update {
                it.copy(
                    amount = amount
                )
            }
            val nmb = amount.toDoubleOrNull()
            if (nmb == null) {
                _state.update {
                    it.copy(
                        amountError = AmountTextError.NOT_NUMBER
                    )
                }
            } else {
                if (nmb < pps.promiseMin) {
                    _state.update {
                        it.copy(
                            amountError = AmountTextError.LESS_MIN
                        )
                    }
                } else if (nmb > pps.promiseMax) {
                    _state.update {
                        it.copy(
                            amountError = AmountTextError.MORE_MAX
                        )
                    }
                } else if (pps.promiseRent && nmb > success.promisePayment) {
                    _state.update {
                        it.copy(
                            amountError = AmountTextError.MORE_RECOMMENDED
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            amountError = AmountTextError.NO_ERROR
                        )
                    }
                }
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
}