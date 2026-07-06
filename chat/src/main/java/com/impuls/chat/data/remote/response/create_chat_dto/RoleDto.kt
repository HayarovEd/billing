package com.impuls.chat.data.remote.response.create_chat_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoleDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)