package com.impuls.chat.domain.model

import kotlinx.datetime.LocalDateTime

abstract class BaseMessage(
    open val date: LocalDateTime,
    open val id: String,
    open val isSupport: Boolean,
    open val text: String,
    open val statusMessage: StatusMessage,
) {
}