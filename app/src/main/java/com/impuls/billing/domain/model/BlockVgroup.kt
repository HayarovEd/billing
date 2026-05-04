package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime


data class BlockVgroup(
    val agrmId: Long,
    val agrmNum: String,
    val blkReq: TypeBlockVGroup,
    val changeTime: LocalDateTime,
    val comment: String,
    val freewill: Boolean,
    val groupId: Long,
    val id: Long,
    val login: String,
    val mgrDescr: String,
    val mgrName: String,
    val recordId: Long,
    val timeTo: LocalDateTime,
    val uid: Long,
    val unBlockedManagerDescr: String,
    val unBlockedManagerLogin: String,
    val unBlockedManagerName: String,
    val username: String,
    val vgId: Long
)
