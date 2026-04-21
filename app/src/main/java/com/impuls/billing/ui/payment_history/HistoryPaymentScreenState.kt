package com.impuls.billing.ui.payment_history

import com.impuls.billing.R
import com.impuls.billing.domain.model.HistoryDebit
import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.model.PeriodHistory

data class HistoryPaymentScreenState(
    val agrmId: Long,
    val historyPayments: List<HistoryPayment> = emptyList(),
    val historyDebits: List<HistoryDebit> = emptyList(),
    val isLoading: Boolean = false,
    val periodHistory: PeriodHistory = PeriodHistory.Today,
    val typeScreen: TypeScreen = TypeScreen.PAYMENT
) {
    val paymentsSum = historyPayments.sumOf { it.amount }
    val debitsSum = historyDebits.sumOf { it.amount }
}


enum class TypeScreen(
    val iconRes: Int,
    val titleRes: Int,
) {
    WRITEOFF(
        iconRes = R.drawable.payment_arrow_down_24dp,
        titleRes = R.string.debits
    ),
    PAYMENT(
        iconRes = R.drawable.add_card_24dp,
        titleRes = R.string.deposits
    )
}

enum class TypeBottomSheet{
    PERIODS,
    CALENDAR
}