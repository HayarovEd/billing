package com.impuls.billing.ui.settings_screen

import com.edurda77.download_install.utils.UiResultText
import com.impuls.billing.ui.uikit.UiText

sealed interface SettingsScreenEvent {
    data class OnMessage(val message: UiText) :SettingsScreenEvent
    data class OnUpdateMessage(val message: UiResultText) :SettingsScreenEvent
    data object NavigateEvent : SettingsScreenEvent
}