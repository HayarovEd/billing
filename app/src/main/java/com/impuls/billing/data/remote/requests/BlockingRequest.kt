package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockingRequest(
    @SerialName("agrmId")
    val agrmId: Long,
    @SerialName("blkReq")
    val blkReq: Int,
    @SerialName("changeTime")
    val changeTime: String,
    @SerialName("vgId")
    val vgId: Long
)