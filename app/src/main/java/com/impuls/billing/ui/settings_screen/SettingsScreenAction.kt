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

    class UpdatePhone(val phone: String): SettingsScreenAction
    data object ResentCodePhone: SettingsScreenAction
    class ConfirmPhone(val code: String): SettingsScreenAction
    class UpdateEmail(val email: String): SettingsScreenAction
    data object ResentCodeEmail: SettingsScreenAction
    class ConfirmEmail(val code: String): SettingsScreenAction

    class UpdatePassword(
        val old: String,
        val new: String,
        val confirm: String,
    ) : SettingsScreenAction

    data object LogOff: SettingsScreenAction
}