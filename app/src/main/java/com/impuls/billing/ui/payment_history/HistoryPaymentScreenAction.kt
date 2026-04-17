package com.impuls.billing.ui.payment_history

import com.impuls.billing.domain.model.PeriodHistory

sealed interface HistoryPaymentScreenAction {
    class getHistoryByPeriod(val periodHistory: PeriodHistory): HistoryPaymentScreenAction
    class SwitchTypeScreen(val typeScreen: TypeScreen): HistoryPaymentScreenAction
}