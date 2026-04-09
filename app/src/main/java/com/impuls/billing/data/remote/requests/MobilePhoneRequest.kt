package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobilePhoneRequest(
    @SerialName("phone")
    val phone: String,
    @SerialName("uid")
    val uid: Long
)