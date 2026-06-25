package com.impuls.chat.domain.model

sealed interface WebSocketChatMessage {
    class UpdateChatMessage(val chat: Chat): WebSocketChatMessage
}