package com.impuls.billing.data.remote.client_account


data class UserGroupsDto(
    val usergroups: List<UserGroupDto>,
    val usercnt: Int,
    val fread: Int,
    val fwrite: Int,
    val defaultgroup: Int
)
