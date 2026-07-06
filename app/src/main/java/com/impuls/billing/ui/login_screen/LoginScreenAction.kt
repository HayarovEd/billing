package com.impuls.billing.ui.login_screen

sealed interface LoginScreenAction {
    class UpdateLogin(val login: String): LoginScreenAction
    class UpdatePassword(val password: String): LoginScreenAction
    class UpdateRecoverType(val recoverType: RecoverType): LoginScreenAction
    data object RecoverPassword: LoginScreenAction
    data object OnLogin: LoginScreenAction
    class ConfirmWithCode(val code: String, val password: String): LoginScreenAction
}