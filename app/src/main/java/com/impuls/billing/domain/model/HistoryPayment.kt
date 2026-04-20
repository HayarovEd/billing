package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime

data class HistoryPayment(
    val timestamp: LocalDateTime,
    val amount: Double,
    val uid: Long,
    val status: HistoryStatusPayment,
    val message: String,
    val currency: String,
)
