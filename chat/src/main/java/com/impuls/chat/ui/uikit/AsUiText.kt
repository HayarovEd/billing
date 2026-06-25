package com.impuls.chat.ui.uikit

import com.impuls.billing.ui.uikit.UiText
import com.impuls.billing.ui.uikit.UiText.*
import com.impuls.chat.R
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import com.impuls.chat.ui.main_screen.MessageErrorEvent


internal fun ChatDataError.asUiText(): UiText {
    return when (this) {

        ChatDataError.Network.NO_INTERNET -> StringResource(
            R.string.no_internet
        )

        ChatDataError.Network.SERVER_ERROR -> StringResource(
            R.string.server_error
        )

        ChatDataError.Network.UNKNOWN -> StringResource(
            R.string.unknown_error
        )


        ChatDataError.Network.UNAUTHORIZED -> StringResource(
            R.string.error_autorization
        )



        ChatDataError.Network.BAD_REQUEST -> {
            StringResource(
                R.string.not_unique_id
            )
        }

        ChatDataError.Network.REQUEST_TIMEOUT -> {
            StringResource(
                R.string.request_timeout
            )
        }

        ChatDataError.Network.NOT_FOUND -> {
            StringResource(
                R.string.not_found
            )
        }
        ChatDataError.WebSocketErrorChat.NOT_CONNECT -> {
            StringResource(
                R.string.lost_connect
            )
        }
        is ChatDataError.CustomErrorChat -> DynamicString(message)
    }
}

internal fun ResultWork.Error<*, ChatDataError>.asErrorUiText(): UiText {
    return error.asUiText()
}


internal fun MessageErrorEvent.asText(): UiText {
    return when (this) {
        MessageErrorEvent.MESSAGE_EMPTY -> {
            StringResource(
                R.string.write_message
            )
        }
        MessageErrorEvent.WRONG_EMAIL -> {
            StringResource(
                R.string.wrong_email
            )
        }
        MessageErrorEvent.PHONE_EMPTY -> {
            StringResource(
                R.string.phone_is_empty
            )
        }
        MessageErrorEvent.NAME_EMPTY -> {
            StringResource(
                R.string.name_is_empty
            )
        }
        MessageErrorEvent.SELECTED_THEME_NULL -> {
            StringResource(
                R.string.theme_not_choosed
            )
        }
    }
}

