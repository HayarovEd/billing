package com.impuls.chat.data.remote.response.create_chat_dto


import com.impuls.chat.data.remote.response.SubgroupDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    @SerialName("connections")
    val connections: List<ConnectionDto>,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: String,
    @SerialName("isOpen")
    val isOpen: Boolean,
    @SerialName("isWork")
    val isWork: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("subgroup")
    val subgroup: SubgroupDto
)