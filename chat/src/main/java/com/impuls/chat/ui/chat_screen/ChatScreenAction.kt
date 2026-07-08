package com.impuls.chat.ui.chat_screen

sealed interface ChatScreenAction {
    class SendMessage(val message: String) : ChatScreenAction
    object CloseChat : ChatScreenAction
    class UpdateCurrentMessage(val message: String) : ChatScreenAction
    class SetMessageAsRead (val messageId: String) : ChatScreenAction
    data class ViewMedia(val file: com.impuls.chat.domain.model.MessageFile?) : ChatScreenAction
}