package com.impuls.billing.domain.model

data class ClientAccount(
    val billDeliveryName: String,
    val account: Account,
    //val groups: UserGroups,
    val agreement: Agreement
)
