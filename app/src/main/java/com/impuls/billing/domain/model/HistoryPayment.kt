package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime

data class HistoryPayment(
    val timestamp: LocalDateTime,
    val amount: Double,
    val agrmId: Long,
    val status: HistoryStatusPayment
)
