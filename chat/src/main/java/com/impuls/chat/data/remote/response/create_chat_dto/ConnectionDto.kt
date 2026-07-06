package com.impuls.chat.data.remote.response.create_chat_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionDto(
    @SerialName("end")
    val end: String?,
    @SerialName("id")
    val id: String,
    @SerialName("messages")
    val messages: List<MessageDto>? = emptyList(),
    @SerialName("start")
    val start: String,
    @SerialName("user")
    val user: UserDto?
)