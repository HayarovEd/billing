package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecoverPasswordDto(
    @SerialName("destination")
    val destination: String,
    @SerialName("recordId")
    val recordId: Long
)