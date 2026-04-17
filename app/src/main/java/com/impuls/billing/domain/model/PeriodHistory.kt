package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime

sealed interface PeriodHistory {
    data object Today : PeriodHistory
    data object LastWeek : PeriodHistory
    data object LastMonth : PeriodHistory
    class SelectedPeriod(
        val dateFrom: LocalDateTime,
        val dateTo: LocalDateTime
    ) : PeriodHistory
}