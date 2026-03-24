package com.impuls.billing.domain.utils

import com.impuls.billing.domain.model.BillDeliveryType
import com.impuls.billing.domain.model.PaymentMethodTYpe
import com.impuls.billing.domain.model.UserType


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

fun convertPaymentMethodTYpe(number: Int): PaymentMethodTYpe {
    return when (number) {
        0 -> PaymentMethodTYpe.ADVANCE
        1 -> PaymentMethodTYpe.CREDIT
        2 -> PaymentMethodTYpe.MIXED
        else -> PaymentMethodTYpe.ADVANCE
    }
}