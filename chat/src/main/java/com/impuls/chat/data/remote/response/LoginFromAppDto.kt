package com.impuls.chat.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginFromAppDto(
    @SerialName("token")
    val token: String
)