package com.impuls.billing.domain.model

enum class OwnerShipType (
    val number: Int
){
    NOT_INDICATED(0),
    OOO(1),
    OAO(2),
    ZAO(3),
}