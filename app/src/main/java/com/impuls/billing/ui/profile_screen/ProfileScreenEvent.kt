package com.impuls.billing.ui.profile_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface ProfileScreenEvent {
    data class OnMessage(val message: UiText) : ProfileScreenEvent
    data object NavigateEvent : ProfileScreenEvent
}