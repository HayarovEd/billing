package com.impuls.billing.data.remote.payment_card


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultCardDto(
    @SerialName("code")
    val code: Int,
    @SerialName("details")
    val details: String,
    @SerialName("message")
    val message: String?,
)