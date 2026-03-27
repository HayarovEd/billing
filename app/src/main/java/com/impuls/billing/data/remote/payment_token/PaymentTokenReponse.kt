package com.impuls.billing.data.remote.payment_token


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentTokenReponse(
    @SerialName("token")
    val token: String,
    @SerialName("ttl")
    val ttl: String
)