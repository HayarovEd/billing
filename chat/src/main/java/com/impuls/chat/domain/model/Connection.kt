package com.impuls.chat.domain.model

import kotlinx.datetime.LocalDateTime

data class Connection(
    val end: LocalDateTime?,
    val id: String,
    val messages: List<Message>,
    val start: LocalDateTime,
    val user: User?
)
