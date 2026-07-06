package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PPSettingsDto(
    @SerialName("promise_available")
    val promiseAvailable: Boolean,
    @SerialName("promise_block_days")
    val promiseBlockDays: Int,
    @SerialName("promise_fee")
    val promiseFee: Double,
    @SerialName("promise_limit")
    val promiseLimit: Double,
    @SerialName("promise_max")
    val promiseMax: Double,
    @SerialName("promise_min")
    val promiseMin: Double,
    @SerialName("promise_on_days")
    val promiseOnDays: Int,
    @SerialName("promise_rent")
    val promiseRent: Boolean,
    @SerialName("promise_till")
    val promiseTill: Int
)