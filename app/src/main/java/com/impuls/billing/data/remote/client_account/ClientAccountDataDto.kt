package com.impuls.billing.data.remote.client_account

import kotlinx.serialization.Serializable

@Serializable
data class ClientAccountDataDto(
    val application: Int,
    val billdeliveryname: String,
    val account: AccountDto,
    val usergroups: UserGroupsDto,
    val agreements: AgreementDto
)
