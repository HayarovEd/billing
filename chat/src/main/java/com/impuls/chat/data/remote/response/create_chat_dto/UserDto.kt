package com.impuls.chat.data.remote.response.create_chat_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: String,
    @SerialName("isDisabled")
    val isDisabled: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: RoleDto
)