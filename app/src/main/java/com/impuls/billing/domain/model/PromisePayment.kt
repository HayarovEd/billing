package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime

data class PromisePayment(
    val agrmId: Int,
    val agrmNum: String,
    val amount: Double,
    val cancelFio: String,
    val cancelPerson: Int,
    val currId: Int,
    val debt: Double,
    val fee: Double,
    val modFio: String,
    val modPerson: Int,
    val payId: Int,
    val promiseDate: LocalDateTime,
    val promiseTill: LocalDateTime,
    val recordId: Int,
    val status: Int,
    val symbol: String,
    val username: String
)
