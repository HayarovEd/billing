package com.impuls.billing.domain.model.payment


data class ResultCard(
    val code: Int,
    val details: String,
    val message: String?,
)
