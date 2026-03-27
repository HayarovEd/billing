package com.impuls.billing.data.remote.payment.base_data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermDto(
    @SerialName("recipient")
    val recipient: String
)