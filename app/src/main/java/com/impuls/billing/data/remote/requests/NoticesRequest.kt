package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticesRequest(
    @SerialName("descr")
    val descr: String,
    @SerialName("isEmail")
    val isEmail: Boolean,
    @SerialName("isMessenger")
    val isMessenger: Boolean,
    @SerialName("isSms")
    val isSms: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: Int,
    @SerialName("value")
    val value: String
)