package com.impuls.billing.data.remote.payment_sbp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WayToPaySbpDto(
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String,
)