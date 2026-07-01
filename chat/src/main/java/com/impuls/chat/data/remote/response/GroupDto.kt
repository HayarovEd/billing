package com.impuls.chat.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)