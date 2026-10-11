package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuccessDto(
    @SerialName("success")
    val success: Boolean
)