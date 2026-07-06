package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromisePaymentRequest(
    @SerialName("agreementId")
    val agreementId: Long,
    @SerialName("sum")
    val sum: Double
)