package com.impuls.billing.domain.model.payment

data class ResultCheck(
    val code: Int,
    val details: String?,
    val message: String?,
)
