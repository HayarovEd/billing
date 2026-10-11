package com.impuls.billing.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ClientShortStatDto(
    val agrmId: Long?,
    val vgId: Long?,
    val agentId: Long?,
    val agentType: Long?,
    val contIn: Long?,
    val cntOut: Long?,
    val duration: Long?,
    val amount: Double?,
    val dt: String?,
    val agentDescr: String?,
    val currSymbol: String?,
    val volume: Double?,
    val volumeIn: Double?,
    val volumeOut: Double?,
    val total: Long?
)
