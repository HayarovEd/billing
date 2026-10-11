package com.impuls.billing.data.remote.payment_sbp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSbpResponse(
    @SerialName("failUrl")
    val failUrl: String?,
    @SerialName("receipt")
    val receipt: ReceiptSbpDto,
    @SerialName("result")
    val result: ResultSbpDto,
    @SerialName("successUrl")
    val successUrl: String?,
    @SerialName("wayToPay")
    val wayToPay: WayToPaySbpDto
)