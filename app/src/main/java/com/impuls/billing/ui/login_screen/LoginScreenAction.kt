package com.impuls.billing.ui.login_screen

sealed interface LoginScreenAction {
    class UpdateEmail(val email: String): LoginScreenAction
    class UpdatePassword(val password: String): LoginScreenAction
    data object OnLogin: LoginScreenAction
}