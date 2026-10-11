package com.impuls.billing.domain.model

enum class BillDeliveryType(val number:Int) {
    COURIER(0),
    MAIL(1),
    YOURSELF(2),
    OTHER(3),
    EMAIL(4),
}