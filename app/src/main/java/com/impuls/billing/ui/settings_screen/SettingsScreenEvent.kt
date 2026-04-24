package com.impuls.billing.ui.settings_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface SettingsScreenEvent {
    data class OnMessage(val message: UiText) :SettingsScreenEvent
    data object NavigateEvent : SettingsScreenEvent
}