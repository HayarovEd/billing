package com.impuls.chat.ui.chat_screen

import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes

sealed interface ChatScreenAction {
    class SendMessage(val message: String) : ChatScreenAction
    object CloseChat : ChatScreenAction
    class UpdateCurrentMessage(val message: String) : ChatScreenAction
    class SetMessageAsRead (val messageId: String) : ChatScreenAction
    data class ViewMedia(val file: MessageFile?) : ChatScreenAction
    data class OpenFileAnotherApp(
        val fileName: String,
        val originalName: String,
        val mimeType: MimeTypes.MimeType
    ) : ChatScreenAction
    data class SaveFileLocal(
        val fileName: String,
        val originalName: String,
    ) : ChatScreenAction
}