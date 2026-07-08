package com.impuls.chat.domain.model


data class MessageFile(
    val fileSize: Long,
    val imageUrl: String,
    val mimeType: MimeEntry?
)
