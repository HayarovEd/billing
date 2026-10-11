package com.impuls.chat.domain.model

import kotlinx.datetime.LocalDateTime


data class Chat(
    val connections: List<Connection>,
    val createdDate: LocalDateTime,
    val email: String,
    val id: String,
    val isOpen: Boolean,
    val isWork: Boolean,
    val name: String,
    val login: String,
    val phone: String,
    val group: Group,
    val isActive: Boolean,
    val unreadMessagesCount: Int,
)
