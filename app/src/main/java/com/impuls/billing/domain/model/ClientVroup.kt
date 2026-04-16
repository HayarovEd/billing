package com.impuls.billing.domain.model


data class ClientVroup(
    val agrmId: Long,
    val vgId: Long,
    val blocked: Boolean,
    val tarifId: Long,
    val tarifDescr: String,
    val agentDescr: String,
    val login: String,
    val serviceVolume: Long,
    val serviceRent: Double,
    val currentModifier: ModifierData,
    val addresses: List<String>,
)

data class ModifierData(
    val type: String,
    val value: Double
)