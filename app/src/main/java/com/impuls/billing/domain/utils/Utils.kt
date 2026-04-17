package com.impuls.billing.domain.utils

import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.HistoryStatusPayment
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.domain.model.PeriodHistory
import com.impuls.billing.domain.model.UserType
import com.impuls.billing.domain.model.payment.StatusPayment
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant


fun findInXmlString(
    tag: String,
    xml: String
): String? {
    val pattern = "<$tag>(.*?)</$tag>".toRegex(RegexOption.DOT_MATCHES_ALL)
    return pattern.find(xml)?.groupValues?.get(1)
}

fun convertBillDeliveryType(number: Long): BillDeliveryType {
    return when (number) {
        0L -> BillDeliveryType.COURIER
        1L -> BillDeliveryType.MAIL
        2L -> BillDeliveryType.YOURSELF
        4L -> BillDeliveryType.EMAIL
        else -> BillDeliveryType.OTHER
    }
}

fun convertUserType(number: Long): UserType {
    return when (number) {
        1L -> UserType.UL
        else -> UserType.FL
    }
}

fun convertToBoolean(number: Long): Boolean {
    return when (number) {
        1L -> true
        else -> false
    }
}

fun convertPaymentMethodTYpe(number: Long): PaymentMethodType {
    return when (number) {
        0L -> PaymentMethodType.ADVANCE
        1L -> PaymentMethodType.CREDIT
        2L -> PaymentMethodType.MIXED
        else -> PaymentMethodType.ADVANCE
    }
}

fun String.toLocalDateTime(): LocalDateTime {
    val parts = split(" ")
    val datePart = parts[0]
    val timePart = parts[1]

    val dateParts = datePart.split("-")
    val formattedDate = "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"

    return LocalDateTime.parse("${formattedDate}T${timePart}")
}

fun convertToStatusPayment(string: String): StatusPayment {
    return when (string.lowercase()) {
        "created_error" -> StatusPayment.CREATED_ERROR
        "created" -> StatusPayment.CREATED
        "rejected" -> StatusPayment.REJECTED
        "refunded" -> StatusPayment.REFUNDED
        "payed" -> StatusPayment.PAYED
        "holded" -> StatusPayment.HOLDED
        "processed" -> StatusPayment.PROCESSED
        "error" -> StatusPayment.ERROR
        else -> StatusPayment.ERROR
    }
}

fun convertToHistoryStatusPayment(number: Long): HistoryStatusPayment {
    return when (number) {
        0L -> HistoryStatusPayment.APPROVED
        1L -> HistoryStatusPayment.APPROVED_ON_FILE
        2L -> HistoryStatusPayment.CANCELLED
        else -> HistoryStatusPayment.CANCELLED
    }
}

fun LocalDate.toBillingFormat(): String {
    return "${this.year}-${this.month.number}-${day}"
}

fun PeriodHistory.convertToPairDate(): Pair<LocalDate, LocalDate> {
    val today = Clock.System.now()
    return when (this) {
        PeriodHistory.LastMonth -> Pair(
            today.minus(1, DateTimeUnit.MONTH, TimeZone.UTC).toCurrentLocalDateTime(),
            today.toCurrentLocalDateTime()
        )

        PeriodHistory.LastWeek -> Pair(
            today.minus(1, DateTimeUnit.WEEK, TimeZone.UTC).toCurrentLocalDateTime(),
            today.toCurrentLocalDateTime()
        )

        is PeriodHistory.SelectedPeriod -> Pair(this.dateFrom, this.dateTo)
        PeriodHistory.Today -> Pair(
            today.minus(1, DateTimeUnit.DAY, TimeZone.UTC).toCurrentLocalDateTime(),
            today.toCurrentLocalDateTime()
        )
    }
}


fun Instant.toCurrentLocalDateTime(): LocalDate {
    return this.toLocalDateTime(TimeZone.currentSystemDefault()).date
}