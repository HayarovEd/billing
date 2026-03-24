package com.impuls.billing.data.remote.client_account.old


data class ClientAccountDataDto(
    val application: Int,
    val billdeliveryname: String,
    val account: AccountDto,
    val usergroups: UserGroupsDto,
    val agreements: List<AgreementDto>
)
