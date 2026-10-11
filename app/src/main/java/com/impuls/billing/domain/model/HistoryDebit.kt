package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDate

data class HistoryDebit(
    val amount: Double,
    val yearMonth: LocalDate
)
