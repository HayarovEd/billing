package com.impuls.billing.ui.settings_screen

import com.impuls.billing.domain.model.ClientInfo
import com.impuls.billing.domain.model.ClientNotices

data class SettingsScreenState(
    val clientInfo: ClientInfo? = null,
    val clientNotices: ClientNotices? = null,
    val isLoading: Boolean = false,
)


enum class TypeNotice {
    WARNING_BALANCE,
    END_PROMISE_PAYMENT,
    MONTHLY_REPORT,
    PAYMENT,
    MONTHLY_REPORT_ACCOUNT_BALANCE,
    BLOCK_ON,
    BLOCK_OFF
}
