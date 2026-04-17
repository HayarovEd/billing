package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDate

sealed interface PeriodHistory {
    data object Today : PeriodHistory
    data object LastWeek : PeriodHistory
    data object LastMonth : PeriodHistory
    class SelectedPeriod(
        val dateFrom: LocalDate,
        val dateTo: LocalDate
    ) : PeriodHistory
}