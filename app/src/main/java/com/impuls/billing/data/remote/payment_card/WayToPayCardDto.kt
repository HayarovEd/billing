package com.impuls.billing.data.remote.payment_card


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WayToPayCardDto(
    @SerialName("methodType")
    val methodType: String,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String
)