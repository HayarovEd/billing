package com.impuls.billing.ui.profile_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface ProfileScreenEvent {
    data class OnMessage(val message: UiText) : ProfileScreenEvent

    //data class onResultUpdatePassword(val message: UiText) : ProfileScreenEvent
    data object NavigateEvent : ProfileScreenEvent
    data class NavigateChat(
        val chatId: String,
        val name: String,
        val login: String,
        val password: String,
        val email: String,
        val billingToken: String,
    ) : ProfileScreenEvent
}