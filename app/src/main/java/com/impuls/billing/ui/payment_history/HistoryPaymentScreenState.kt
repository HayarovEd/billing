package com.impuls.billing.ui.payment_history

import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.model.PeriodHistory

data class HistoryPaymentScreenState(
    val agrmId: Long,
    val historyPayments: List<HistoryPayment> = emptyList(),
    val isLoading: Boolean = false,
    val periodHistory: PeriodHistory = PeriodHistory.Today,
    val typeScreen: TypeScreen = TypeScreen.PAYMENT
)


enum class TypeScreen {
    WRITEOFF,
    PAYMENT
}