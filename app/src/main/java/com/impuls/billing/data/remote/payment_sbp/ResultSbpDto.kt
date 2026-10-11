package com.impuls.billing.data.remote.payment_sbp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultSbpDto(
    @SerialName("code")
    val code: Int,
    @SerialName("details")
    val details: String,
    @SerialName("message")
    val message: String?,
)