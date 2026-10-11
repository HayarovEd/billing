package com.impuls.chat.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    @SerialName("text")
    val text: String
)