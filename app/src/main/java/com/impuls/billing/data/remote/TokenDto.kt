package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    @SerialName("id")
    val id: Int,
    @SerialName("token")
    val token: String
)