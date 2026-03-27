package com.impuls.billing.domain.model.payment

enum class StatusPayment {
    CREATED_ERROR,
    CREATED,
    REJECTED,
    REFUNDED,
    PAYED,
    HOLDED,
    PROCESSED,
    ERROR
}