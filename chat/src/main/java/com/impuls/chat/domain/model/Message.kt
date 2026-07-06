package com.impuls.chat.domain.model

import kotlinx.datetime.LocalDateTime


data class Message(
    val date: LocalDateTime,
    val id: String,
    val isSupport: Boolean,
    val text: String,
    val statusMessage: StatusMessage
)
