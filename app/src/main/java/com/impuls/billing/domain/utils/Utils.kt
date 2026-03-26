package com.impuls.billing.domain.utils

import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.PaymentMethodType
import com.impuls.billing.domain.model.UserType
import kotlinx.datetime.LocalDateTime


fun findInXmlString(
    tag: String,
    xml: String
): String? {
    val pattern = "<$tag>(.*?)</$tag>".toRegex(RegexOption.DOT_MATCHES_ALL)
    return pattern.find(xml)?.groupValues?.get(1)
}

fun convertBillDeliveryType(number: Int): BillDeliveryType {
    return when (number) {
        0 -> BillDeliveryType.COURIER
        1 -> BillDeliveryType.MAIL
        2 -> BillDeliveryType.YOURSELF
        4 -> BillDeliveryType.EMAIL
        else -> BillDeliveryType.OTHER
    }
}

fun convertUserType(number: Int): UserType {
    return when (number) {
        1 -> UserType.UL
        else -> UserType.FL
    }
}

fun convertToBoolean(number: Int): Boolean {
    return when (number) {
        1 -> true
        else -> false
    }
}

fun convertPaymentMethodTYpe(number: Int): PaymentMethodType {
    return when (number) {
        0 -> PaymentMethodType.ADVANCE
        1 -> PaymentMethodType.CREDIT
        2 -> PaymentMethodType.MIXED
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