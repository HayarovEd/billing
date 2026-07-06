package com.impuls.billing.ui.blocking_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface BlockingScreenEvent {
    data class OnMessage(val message: UiText) : BlockingScreenEvent
    data object NavigateEvent : BlockingScreenEvent
}