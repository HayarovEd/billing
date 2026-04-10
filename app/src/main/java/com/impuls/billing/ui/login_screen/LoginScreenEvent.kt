package com.impuls.billing.ui.login_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface LoginScreenEvent {
    data class OnMessage(val message: UiText) : LoginScreenEvent
    data object NavigateEvent : LoginScreenEvent
    data object openConfirmDialog : LoginScreenEvent
}