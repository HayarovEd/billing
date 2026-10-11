package com.impuls.billing.ui.login_screen

data class LoginScreenState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val destination: String = "",
    val type: RecoverType = RecoverType.SMS,
    val count: Int = 0,
)