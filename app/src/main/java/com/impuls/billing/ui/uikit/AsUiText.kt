package com.impuls.billing.ui.uikit

import com.impuls.billing.R
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork


fun DataError.asUiText(): UiText {
    return when (this) {

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Network.UNKNOWN -> UiText.DynamicString("")

        DataError.ValidationError.EMAIL_BLANK -> UiText.StringResource(
            R.string.email_empty
        )
        DataError.ValidationError.EMAIL_NOT_VALID -> UiText.StringResource(
            R.string.email_unvalid
        )
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(
            R.string.error_autorization
        )
        DataError.ValidationError.PASSWORD_BLANK -> UiText.StringResource(
            R.string.password_blank
        )


        DataError.Network.BAD_REQUEST -> {
            UiText.StringResource(
                R.string.not_unique_id
            )
        }
        DataError.DataStore.ERROR_READ_DATA -> {
            UiText.StringResource(
                R.string.read_error_data
            )
        }

        DataError.DataStore.ERROR_WRITE_DATA -> {
            UiText.StringResource(
                R.string.write_error_data
            )
        }

        DataError.SerializationError.FORMAT_ERROR -> {
            UiText.StringResource(
                R.string.error_take_data
            )
        }



        DataError.LocalDateBase.ERROR_READ_DATA -> {
            UiText.StringResource(
                R.string.read_error_data
            )
        }

        DataError.LocalDateBase.ERROR_WRITE_DATA -> {
            UiText.StringResource(
                R.string.read_error_data
            )
        }

        DataError.Network.REQUEST_TIMEOUT -> {
            UiText.StringResource(
                R.string.request_timeout
            )
        }
    }
}

fun ResultWork.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}