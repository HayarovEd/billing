package com.impuls.billing.data.remote.payment_card


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayCardRequest(
    @SerialName("amount")
    val amount: Int,
    @SerialName("comission")
    val comission: Int,
    @SerialName("greenZone")
    val greenZone: Boolean,
    @SerialName("offline")
    val offline: Boolean,
    @SerialName("payCurrency")
    val payCurrency: String,
    @SerialName("paymentTool")
    val paymentTool: PaymentToolDto,
    @SerialName("properties")
    val properties: PropertiesCardDto,
)