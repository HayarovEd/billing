package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockingDto(
    @SerialName("agrmId")
    val agrmId: Long,
    @SerialName("agrmNum")
    val agrmNum: String,
    @SerialName("blkReq")
    val blkReq: Long,
    @SerialName("changeTime")
    val changeTime: String,
    @SerialName("comment")
    val comment: String,
    @SerialName("freewill")
    val freewill: Boolean,
    @SerialName("groupId")
    val groupId: Long,
    @SerialName("id")
    val id: Long,
    @SerialName("login")
    val login: String,
    @SerialName("mgrDescr")
    val mgrDescr: String,
    @SerialName("mgrName")
    val mgrName: String,
    @SerialName("recordId")
    val recordId: Long,
    @SerialName("requestBy")
    val requestBy: Long,
    @SerialName("timeTo")
    val timeTo: String,
    @SerialName("uid")
    val uid: Long,
    @SerialName("unBlockedBy")
    val unBlockedBy: Long,
    @SerialName("unBlockedManagerDescr")
    val unBlockedManagerDescr: String,
    @SerialName("unBlockedManagerLogin")
    val unBlockedManagerLogin: String,
    @SerialName("unBlockedManagerName")
    val unBlockedManagerName: String,
    @SerialName("username")
    val username: String,
    @SerialName("vgId")
    val vgId: Long
)