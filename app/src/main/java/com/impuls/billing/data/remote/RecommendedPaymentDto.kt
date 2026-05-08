package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedPaymentDto(
    @SerialName("recommended_payment")
    val recommendedPayment: Double
)