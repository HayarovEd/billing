package com.impuls.chat.data.remote.response.create_chat_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("date")
    val date: String,
    @SerialName("id")
    val id: String,
    @SerialName("isSupport")
    val isSupport: Boolean,
    @SerialName("text")
    val text: String
)