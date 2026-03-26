package com.impuls.billing.domain.model

enum class PaymentMethodType(
    val number: Int
) {
    ADVANCE(0),
    CREDIT(1),
    MIXED(2)
}