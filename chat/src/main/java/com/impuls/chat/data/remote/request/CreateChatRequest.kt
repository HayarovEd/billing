package com.impuls.chat.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    @SerialName("email")
    val email: String,
    @SerialName("message")
    val message: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String
)