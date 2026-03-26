package com.impuls.billing.data.remote.payment_sberpay


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertiesSberDto(
    @SerialName("Л_СЧЕТ")
    val ledgerAccount: String
)