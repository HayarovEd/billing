package com.impuls.billing.data.remote.payment_card


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertiesCardDto(
    @SerialName("Л_СЧЕТ")
    val ledgerAccount: String
)