package com.impuls.billing.data.remote.client_account


data class UserGroupDto(
    val unloadtosorm: Int,
    val sormid: Int,
    val groupid: Int,
    val promiseallow: Int,
    val promiseallowmanager: Int,
    val promiserent: Int,
    val promisetill: Int,
    val promiseondays: Int,
    val promiseblockdays: Int,
    val promisemax: Int,
    val promisemin: Int,
    val promiselimit: Int,
    val name: String,
    val description: String,
    val vendorsormname: String,
    val uuid: String,
    val uniquegroup: Int
)