package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordDto(
    @SerialName("password")
    val password: String
)