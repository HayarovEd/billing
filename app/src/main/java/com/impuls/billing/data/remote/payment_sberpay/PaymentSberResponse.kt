package com.impuls.billing.data.remote.payment_sberpay


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSberResponse(
    @SerialName("failUrl")
    val failUrl: String?,
    @SerialName("receipt")
    val receipt: ReceiptSberDto,
    @SerialName("result")
    val result: ResultSberDto,
    @SerialName("successUrl")
    val successUrl: String?,
    @SerialName("wayToPay")
    val wayToPay: WayToPaySberDto
)