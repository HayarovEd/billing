package com.impuls.chat.ui.chat_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface ChatScreenEvent {
    data class OnMessage(val message: UiText) :ChatScreenEvent
    data object NavigateToLogin : ChatScreenEvent
}