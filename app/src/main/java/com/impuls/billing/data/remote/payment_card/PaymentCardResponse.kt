package com.impuls.billing.data.remote.payment_card


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentCardResponse(
    @SerialName("failUrl")
    val failUrl: String?,
    @SerialName("receipt")
    val receipt: ReceiptCardDto,
    @SerialName("result")
    val result: ResultCardDto,
    @SerialName("successUrl")
    val successUrl: String?,
    @SerialName("wayToPay")
    val wayToPay: WayToPayCardDto
)