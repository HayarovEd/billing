package com.impuls.billing.ui.settings_screen

import com.impuls.billing.domain.model.ClientInfo
import com.impuls.billing.domain.model.ClientNotices

data class SettingsScreenState(
    val clientInfo: ClientInfo? = null,
    val clientNotices: ClientNotices? = null,
    val isLoading: Boolean = false,
    val count: Int = 0,
    val phoneRet: Long? = null,
    val emailRet: Long? = null,
    val isUpdatingPassword: Boolean = false,
)


enum class TypeNotice(val typeNumber: Int) {
    WARNING_BALANCE(1),
    END_PROMISE_PAYMENT(2),
    MONTHLY_REPORT(3),
    PAYMENT(4),
    MONTHLY_REPORT_ACCOUNT_BALANCE(5), //or 6
    BLOCK_ON(7),
    BLOCK_OFF(8)
}
