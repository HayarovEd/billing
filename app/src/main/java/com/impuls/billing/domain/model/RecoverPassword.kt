package com.impuls.billing.domain.model

data class RecoverPassword(
    val destination: String,
    val recordId: Long
)
