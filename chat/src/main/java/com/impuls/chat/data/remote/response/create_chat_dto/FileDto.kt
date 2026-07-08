package com.impuls.chat.data.remote.response.create_chat_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileDto(
    @SerialName("file_size")
    val fileSize: Long,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("mime_type")
    val mimeType: String
)