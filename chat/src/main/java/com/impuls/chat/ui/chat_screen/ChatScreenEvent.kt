package com.impuls.chat.ui.chat_screen

import android.net.Uri
import com.impuls.billing.ui.uikit.UiText
import com.impuls.chat.domain.model.MimeTypes

sealed interface ChatScreenEvent {
    data class OnMessage(val message: UiText) :ChatScreenEvent
    data object NavigateToLogin : ChatScreenEvent
    data class OpenAnotherApp(
        val uri: Uri,
        val mimeType: MimeTypes.MimeType
    ) : ChatScreenEvent
}