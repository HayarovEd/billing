package com.impuls.billing.ui.login_screen

data class LoginScreenState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)