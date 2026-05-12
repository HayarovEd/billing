package com.impuls.billing.ui.uikit

import android.annotation.SuppressLint
import com.impuls.billing.R
import com.impuls.billing.domain.model.PeriodHistory
import com.impuls.billing.domain.model.TypeBlockVGroup
import com.impuls.billing.domain.model.payment.StatusPayment
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.promise_screen.AmountTextError
import com.impuls.billing.ui.settings_screen.TypeNotice
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

        DataError.SuccessWork.SUCCESS_UPDATE_NOTICES -> {
            StringResource(
                R.string.success_update_notices
            )
        }

        DataError.ValidationError.OLD_PASSWORD_INCORRECT -> {
            StringResource(
                R.string.old_passord_incorrect
            )
        }
        DataError.ValidationError.PASSWORDS_NOT_EQUALS -> {
            StringResource(
                R.string.passwords_not_equals
            )
        }

        DataError.SuccessWork.SUCCESS_SET_PROMISE_PAYMENT -> {
            StringResource(
                R.string.success_promise_payment
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

@SuppressLint("DefaultLocale")
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
            DynamicString(
                "${String.format("%02d", this.dateFrom.day)}.${
                    String.format(
                        "%02d",
                        this.dateFrom.month.number
                    )
                }-${String.format("%02d", this.dateTo.day)}.${
                    String.format(
                        "%02d",
                        this.dateTo.month.number
                    )
                }"
            )
        }

        PeriodHistory.Today -> StringResource(
            R.string.today
        )
    }
}

fun TypeNotice.asTypeNoticeText(): UiText {
    return when (this) {
        TypeNotice.WARNING_BALANCE -> StringResource(R.string.warning_balance)
        TypeNotice.END_PROMISE_PAYMENT -> StringResource(R.string.end_promise_payment)
        TypeNotice.MONTHLY_REPORT -> StringResource(R.string.monthly_report)
        TypeNotice.PAYMENT -> StringResource(R.string.go_payment)
        TypeNotice.MONTHLY_REPORT_ACCOUNT_BALANCE -> StringResource(R.string.monthly_report_account)
        TypeNotice.BLOCK_ON -> StringResource(R.string.user_block_on)
        TypeNotice.BLOCK_OFF -> StringResource(R.string.user_block_off)
    }
}

fun TypeBlockVGroup.asTypeBlockVGroupText(): UiText {
    return when (this) {
        TypeBlockVGroup.UNBLOCKED -> DynamicString("")
        TypeBlockVGroup.USER_BLOCKED -> StringResource(R.string.blocked_user)
        TypeBlockVGroup.ADMIN_BLOCKED -> StringResource(R.string.blocked_admin)
        TypeBlockVGroup.OFF_ACTIVE -> StringResource(R.string.off_active)
        TypeBlockVGroup.BALANCE_BLOCKED -> StringResource(R.string.blocked_balance)
    }
}

fun AmountTextError.asAmountTextError(): UiText {
    return when(this) {
        AmountTextError.NO_ERROR -> DynamicString("")
        AmountTextError.NOT_NUMBER -> StringResource(R.string.not_number)
        AmountTextError.MORE_MAX -> StringResource(R.string.more_max_promise)
        AmountTextError.LESS_MIN -> StringResource(R.string.less_min_promise)
        AmountTextError.MORE_RECOMMENDED -> StringResource(R.string.more_recomended_promise)
    }
}