package com.impuls.billing.ui.settings_screen

sealed interface SettingsScreenAction {
    class UpdateValue(
        val value: String,
        val typeId: Int
    ) : SettingsScreenAction

    class UpdateCheckSms(
        val isCheckSms: Boolean,
        val typeId: Int
    ) : SettingsScreenAction

    class UpdateCheckEmail(
        val isCheckEmail: Boolean,
        val typeId: Int
    ) : SettingsScreenAction

    data object UpdateNotices : SettingsScreenAction
}