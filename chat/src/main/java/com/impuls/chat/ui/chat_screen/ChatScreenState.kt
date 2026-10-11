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
    val tmpMessage: TmpMessage? = null,
    val token: String = "",
    val selectedFile: MessageFile? = null,
    val attachedFiles: List<Uri> = emptyList(),
    val progressDownload: String = "",
) {
    val allMessages = (collectMessages(chat?.connections ?: emptyList()) + listOfNotNull(tmpMessage))
        .sortedBy { it.date }
        .groupBy { it.date.date }
}