package com.impuls.billing.data.remote.payment_card


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentToolDto(
    @SerialName("type")
    val type: String
)