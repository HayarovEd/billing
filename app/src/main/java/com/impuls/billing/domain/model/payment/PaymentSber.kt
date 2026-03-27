package com.impuls.billing.domain.model.payment

data class PaymentSber(
    val failUrl: String?,
    val receipt: ReceiptCard,
    val result: ResultCard,
    val successUrl: String?,
    val wayToPay: WayToPaySber
)
