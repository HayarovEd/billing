package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DebitDto(
    @SerialName("amount")
    val amount: Double,
    @SerialName("yearMonth")
    val yearMonth: String
)