package com.impuls.billing.ui.payment_history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.billing.domain.model.PeriodHistory
import com.impuls.billing.domain.repository.BillingPaymentRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.utils.convertToPairDate
import com.impuls.billing.ui.uikit.asUiText
import com.impuls.billing.ui.uikit.model.NavigationRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlin.time.Clock

class HistoryPaymentScreenViewModel(
    private val billingPaymentRepository: BillingPaymentRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val agrmId = savedStateHandle.toRoute<NavigationRoute.HistoryPaymentRoute>().agrmId
    private val vgId = savedStateHandle.toRoute<NavigationRoute.HistoryPaymentRoute>().vgId
    private val tariffId = savedStateHandle.toRoute<NavigationRoute.HistoryPaymentRoute>().tariffId

    private val _state = MutableStateFlow(HistoryPaymentScreenState(agrmId = agrmId))
    val state = _state
        .onStart {
            loadData(
                action = {
                    loadInitialData(it)
                },
                navigate = {
                    viewModelScope.launch {
                        eventChannel.send(HistoryPaymentScreenEvent.NavigateEvent)
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryPaymentScreenState(agrmId = agrmId)
        )

    private val eventChannel = Channel<HistoryPaymentScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HistoryPaymentScreenAction) {
        when (action) {
            is HistoryPaymentScreenAction.SwitchTypeScreen -> {
                _state.value.copy(
                    typeScreen = action.typeScreen,
                )
                    .updateState()
            }

            is HistoryPaymentScreenAction.getHistoryByPeriod -> {
                _state.value.copy(
                    periodHistory = action.periodHistory,
                )
                    .updateState()
                loadData(
                    action = {
                        val period = action.periodHistory.convertToPairDate()
                        loadPaymentHistory(
                            token = it,
                            dateFrom = period.first,
                            dateTo = period.second
                        )
                    },
                    navigate = {
                        viewModelScope.launch {
                            eventChannel.send(HistoryPaymentScreenEvent.NavigateEvent)
                        }
                    }
                )
            }
        }
    }

    private fun loadInitialData(token: String) {
        val period = state.value.periodHistory.convertToPairDate()
        loadPaymentHistory(
            token = token,
            dateFrom = period.first,
            dateTo = period.second
        )
    }


    private fun loadPaymentHistory(
        token: String,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ) {
        _state.value.copy(
            isLoading = true,
        )
            .updateState()
        viewModelScope.launch {
            when (val result = billingPaymentRepository.getHistoryOfPayment(
                token = token,
                agrmId = agrmId,
                dateTo = dateTo,
                dateFrom = dateFrom
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                    )
                        .updateState()
                    eventChannel.send(HistoryPaymentScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        historyPayments = result.data,
                    )
                        .updateState()
                }
            }
        }
        viewModelScope.launch {
            val resultDateFrom = when (state.value.periodHistory) {
                PeriodHistory.LastMonth, PeriodHistory.LastWeek, PeriodHistory.Today -> dateFrom
                    .plus(1, DateTimeUnit.DAY)
                is PeriodHistory.SelectedPeriod -> dateFrom
            }
            when (val result = billingPaymentRepository.getHistoryOfDebet(
                token = token,
                agrmId = agrmId,
                dateTo = dateTo,
                dateFrom = resultDateFrom,
                vgId = vgId,
                tarifId = tariffId,
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                    )
                        .updateState()
                    eventChannel.send(HistoryPaymentScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    println(result.data)
                    _state.value.copy(
                        historyDebits = result.data,
                    )
                        .updateState()
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

    private fun HistoryPaymentScreenState.updateState() {
        _state.update {
            this
        }
    }
}