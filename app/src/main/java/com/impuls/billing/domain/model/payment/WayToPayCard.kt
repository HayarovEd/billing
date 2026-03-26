package com.impuls.billing.domain.model.payment

data class WayToPayCard(
    val methodType: String,
    val type: String,
    val url: String
)
