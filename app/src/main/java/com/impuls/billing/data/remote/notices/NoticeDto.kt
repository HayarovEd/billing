package com.impuls.billing.data.remote.notices


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeDto(
    @SerialName("descr")
    val descr: String,
    @SerialName("email")
    val email: Boolean,
    @SerialName("messenger")
    val messenger: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("sms")
    val sms: Boolean,
    @SerialName("type")
    val type: Int,
    @SerialName("value")
    val value: String
)