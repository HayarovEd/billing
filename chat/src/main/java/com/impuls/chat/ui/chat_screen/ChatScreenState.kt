package com.impuls.chat.ui.chat_screen

import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.utils.Utils.collectMessages

data class ChatScreenState(
    val name: String = "",
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val chat: Chat? = null,
    val isLoading: Boolean = false,
    val currentMessage: String = "",
    val tmpMessage: Message? = null,
    val token: String = "",
) {
    val allMessages = collectMessages(chat?.connections ?: emptyList()) + listOfNotNull(tmpMessage)
}