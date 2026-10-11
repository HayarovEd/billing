package com.impuls.chat.data.remote.websocket


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateChatDto  <T>(
    @SerialName("data")
    val remoteData: T,
    @SerialName("event")
    val event: String,
    @SerialName("path")
    val path: String
)
