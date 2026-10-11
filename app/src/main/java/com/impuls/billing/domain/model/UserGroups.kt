package com.impuls.billing.domain.model

data class UserGroups(
    val userCnt: Int,
    val defaultGroup: Int,
    val groups: List<Usergroup>
)
