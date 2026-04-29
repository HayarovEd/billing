package com.impuls.billing.ui.profile_screen

sealed interface ProfileScreenAction {
    class UpdatePhone(val phone: String) : ProfileScreenAction
    data object ResentCodePhone : ProfileScreenAction
    class ConfirmPhone(val code: String) : ProfileScreenAction
    class UpdateEmail(val email: String) : ProfileScreenAction
    data object ResentCodeEmail : ProfileScreenAction
    class ConfirmEmail(val code: String) : ProfileScreenAction
}