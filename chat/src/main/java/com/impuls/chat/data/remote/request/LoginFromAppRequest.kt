package com.impuls.chat.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginFromAppRequest(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String,
    @SerialName("username")
    val username: String
)