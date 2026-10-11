package com.impuls.chat.domain.model

import kotlinx.datetime.LocalDateTime


data class Message(
    override val date: LocalDateTime,
    override val id: String,
    override val isSupport: Boolean,
    override val text: String,
    override val statusMessage: StatusMessage,
    val files: List<MessageFile>,
): BaseMessage(
    date = date,
    id = id,
    isSupport = isSupport,
    text = text,
    statusMessage = statusMessage
)
