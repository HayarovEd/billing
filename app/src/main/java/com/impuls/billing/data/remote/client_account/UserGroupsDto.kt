package com.impuls.billing.data.remote.client_account

import kotlinx.serialization.Serializable

@Serializable
data class UserGroupsDto(
    val usergroup: UserGroupDto,
    val usercnt: Int,
    val fread: Int,
    val fwrite: Int,
    val defaultgroup: Int
)
