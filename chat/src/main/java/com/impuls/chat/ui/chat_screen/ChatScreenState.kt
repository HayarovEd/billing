package com.impuls.chat.ui.chat_screen

import android.net.Uri
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.TmpMessage
import com.impuls.chat.domain.utils.Utils.collectMessages

data class ChatScreenState(
    val chat: Chat? = null,
    val isLoading: Boolean = false,
    val currentMessage: String = "",
    val tmpMessages: List<TmpMessage> = emptyList(),
    val token: String = "",
    val selectedFile: MessageFile? = null,
    val attachedFiles: List<Uri> = emptyList(),
    val progressDownload: String = "",
) {
    val tmpMessage: TmpMessage? get() = tmpMessages.lastOrNull()

    val allMessages = (collectMessages(chat?.connections ?: emptyList()) + tmpMessages)
        .sortedBy { it.date }
        .groupBy { it.date.date }
}