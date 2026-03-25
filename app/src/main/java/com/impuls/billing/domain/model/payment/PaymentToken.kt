package com.impuls.billing.domain.model.payment

import kotlinx.datetime.LocalDateTime

data class PaymentToken(
    val token: String,
    val ttl: LocalDateTime
)
