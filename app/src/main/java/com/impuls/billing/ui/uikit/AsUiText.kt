package com.impuls.billing.ui.uikit

import com.impuls.billing.R
import com.impuls.billing.domain.model.PeriodHistory
import com.impuls.billing.domain.model.payment.StatusPayment
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.UiText.*
import kotlinx.datetime.number


fun DataError.asUiText(): UiText {
    return when (this) {

        DataError.Network.NO_INTERNET -> StringResource(
            R.string.no_internet
        )

        DataError.Network.SERVER_ERROR -> StringResource(
            R.string.server_error
        )

        DataError.Network.UNKNOWN -> StringResource(
            R.string.unknown_error
        )

        DataError.ValidationError.EMAIL_BLANK -> StringResource(
            R.string.email_empty
        )

        DataError.ValidationError.EMAIL_NOT_VALID -> StringResource(
            R.string.email_unvalid
        )

        DataError.Network.UNAUTHORIZED -> StringResource(
            R.string.error_autorization
        )

        DataError.ValidationError.PASSWORD_BLANK -> StringResource(
            R.string.password_blank
        )


        DataError.Network.BAD_REQUEST -> {
            StringResource(
                R.string.not_unique_id
            )
        }

        DataError.DataStore.ERROR_READ_DATA -> {
            StringResource(
                R.string.read_error_data
            )
        }

        DataError.DataStore.ERROR_WRITE_DATA -> {
            StringResource(
                R.string.write_error_data
            )
        }

        DataError.SerializationError.FORMAT_ERROR -> {
            StringResource(
                R.string.error_take_data
            )
        }


        DataError.LocalDateBase.ERROR_READ_DATA -> {
            StringResource(
                R.string.read_error_data
            )
        }

        DataError.LocalDateBase.ERROR_WRITE_DATA -> {
            StringResource(
                R.string.read_error_data
            )
        }

        DataError.Network.REQUEST_TIMEOUT -> {
            StringResource(
                R.string.request_timeout
            )
        }

        DataError.ValidationError.LOGIN_PASSWORD_INCORRECT -> {
            StringResource(
                R.string.login_password_incorrect
            )
        }

        is DataError.CustomError -> DynamicString(message)
        DataError.SuccessWork.SUCCESS_UPDATE_PASSWORD -> {
            StringResource(
                R.string.success_update_password
            )
        }
    }
}

fun ResultWork.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}

fun StatusPayment.asTextMessage(): UiText {
    return when (this) {
        StatusPayment.CREATED_ERROR -> StringResource(
            R.string.error_create
        )

        StatusPayment.PAYED -> StringResource(
            R.string.payment_payed
        )

        StatusPayment.CREATED -> StringResource(
            R.string.payment_created
        )

        StatusPayment.REJECTED -> StringResource(
            R.string.payment_rejected
        )

        StatusPayment.REFUNDED -> StringResource(
            R.string.pay_refunded
        )

        StatusPayment.HOLDED -> StringResource(
            R.string.payment_holded
        )

        StatusPayment.PROCESSED -> StringResource(
            R.string.payment_processed
        )

        StatusPayment.ERROR -> StringResource(
            R.string.payment_error
        )
    }
}

fun PeriodHistory.asPeriodText(): UiText {
    return when (this) {
        PeriodHistory.LastMonth -> {
            StringResource(
                R.string.month
            )
        }

        PeriodHistory.LastWeek -> StringResource(
            R.string.week
        )

        is PeriodHistory.SelectedPeriod -> {
            DynamicString("${this.dateFrom.day}.${this.dateFrom.month.number}-${this.dateTo.day}.${this.dateTo.month.number}")
        }

        PeriodHistory.Today -> StringResource(
            R.string.today
        )
    }
}