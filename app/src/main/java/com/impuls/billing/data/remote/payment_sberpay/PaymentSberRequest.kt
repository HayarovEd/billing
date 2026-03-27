package com.impuls.billing.data.remote.payment_sberpay


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSberRequest(
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
    val paymentTool: PaymentCberToolDto,
    @SerialName("properties")
    val properties: PropertiesSberDto,
)