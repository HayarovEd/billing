package com.impuls.billing.ui.home_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface HomeScreenEvent {
    data class OnMessage(val message: UiText) :HomeScreenEvent
    data object NavigateEvent : HomeScreenEvent
}