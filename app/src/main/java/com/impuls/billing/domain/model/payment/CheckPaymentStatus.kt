package com.impuls.billing.domain.model.payment

import kotlinx.datetime.LocalDateTime

data class CheckPaymentStatus(
    val amount: Int,
    val createDate: LocalDateTime,
    val providerName: String,
    val regPayNum: String,
    val state: StatusPayment,
    val recipient: String
)
