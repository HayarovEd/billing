package com.impuls.chat.domain.model


data class MessageFile(
    val fileSize: Long,
    val fileUrl: String,
    val mimeType: MimeEntry?
)
