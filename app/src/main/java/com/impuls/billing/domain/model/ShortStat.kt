package com.impuls.billing.domain.model

data class ShortStat(
    val agrmId: Long,
    val vgId: Long,
    val amount: Double,
    val agentDescr: String,
    val currSymbol: String,
)
