package com.impuls.billing.ui.blocking_screen

import com.impuls.billing.domain.model.PeriodHistory
import kotlinx.datetime.LocalDate

sealed interface BlockingScreenAction {
    class UpdateSelectedDate(val date: LocalDate): BlockingScreenAction
    class GetHistoryByPeriod(val periodHistory: PeriodHistory): BlockingScreenAction
    data object InsertBlock: BlockingScreenAction
}