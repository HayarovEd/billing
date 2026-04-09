package com.impuls.billing.data.remote.client_account


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientAccountResponse(
    @SerialName("account")
    val account: AccountDto,
    @SerialName("agreements")
    val agreements: List<AgreementDto>,
    @SerialName("billDeliveryName")
    val billDeliveryName: String
)