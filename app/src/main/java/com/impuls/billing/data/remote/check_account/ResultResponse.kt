package com.impuls.billing.data.remote.check_account


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultResponse(
    @SerialName("code")
    val code: Int,
    @SerialName("details")
    val details: String?,
    @SerialName("message")
    val message: String?,
)