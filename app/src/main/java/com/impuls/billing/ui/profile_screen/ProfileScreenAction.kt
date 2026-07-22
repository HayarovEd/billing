package com.impuls.billing.ui.profile_screen

import com.impuls.chat.service.FcmMessageData

sealed interface ProfileScreenAction {
    class UpdatePhone(val phone: String) : ProfileScreenAction
    data object ResentCodePhone : ProfileScreenAction
    class ConfirmPhone(val code: String) : ProfileScreenAction
    class UpdateEmail(val email: String) : ProfileScreenAction
    data object ResentCodeEmail : ProfileScreenAction
    class ConfirmEmail(val code: String) : ProfileScreenAction
    class FcmMessage(val fcmMessageData: FcmMessageData) : ProfileScreenAction
}