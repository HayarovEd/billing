package com.impuls.billing.data.remote.check_status_payment


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermDto(
    @SerialName("recipient")
    val recipient: String
)