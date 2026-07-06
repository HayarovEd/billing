package com.impuls.chat.data.remote.response.create_chat_dto


import com.impuls.chat.data.remote.response.GroupDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    @SerialName("connections")
    val connections: List<ConnectionDto>,
    @SerialName("created_date")
    val createdDate: String,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: String,

    @SerialName("is_open")
    val isOpen: Boolean,
    @SerialName("is_work")
    val isWork: Boolean,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("unread_messages_count")
    val unreadMessagesCount: Int,
    @SerialName("name")
    val name: String,
    @SerialName("login")
    val login: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("group")
    val group: GroupDto
)