package com.impuls.billing.data.remote.client_account


data class ClientAccountDataDto(
    val application: Int,
    val billdeliveryname: String,
    val account: AccountDto,
    val usergroups: UserGroupsDto,
    val agreements: List<AgreementDto>
)
