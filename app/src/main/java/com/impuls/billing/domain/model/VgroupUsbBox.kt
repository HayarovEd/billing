package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime

data class VgroupUsbBox(
    val activated: LocalDateTime,
    val state: Long,
    val rate: Double,
    val price: Double,
    val vgLogin: String,
    val tarDescr: String,
    val symbol: String,
)
