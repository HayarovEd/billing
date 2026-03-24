package com.impuls.billing.domain.model

enum class PaymentMethodTYpe(
    val number: Int
) {
    ADVANCE(0),
    CREDIT(1),
    MIXED(2)
}