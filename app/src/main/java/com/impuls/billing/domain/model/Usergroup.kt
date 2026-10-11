package com.impuls.billing.domain.model

data class Usergroup(
    val groupId: Long,
    val unloadtoSorm: Boolean,
    val sormId: Long,
    val promiseAllow: Boolean,
    val promiseAllowManager: Boolean,
    val promiseRent: Boolean,
    val promiseTill: Long,
    val promiseOnDays: Int,
    val promiseMax: Double,
    val promiseMin: Double,
    val promiseLimit: Int,
    val name: String,
    val description: String,
    val uuid: String,
)
