package com.impuls.billing.domain.model

data class PPSettings(
    val promiseAvailable: Boolean,
    val promiseBlockDays: Int,
    val promiseFee: Double,
    val promiseLimit: Double,
    val promiseMax: Double,
    val promiseMin: Double,
    val promiseOnDays: Int,
    val promiseRent: Boolean,
    val promiseTill: Int
)
