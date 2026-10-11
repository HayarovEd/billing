package com.impuls.billing.domain.model.payment

data class Comission(
    val comissionType: String,
    val maxAmount: Long?,
    val maxPayAmount: Int,
    val minAmount: Long?,
    val minPayAmount: Int,
    val payCurrency: String,
    val payType: String,
    val termDto: Term,
    val value: String
)
