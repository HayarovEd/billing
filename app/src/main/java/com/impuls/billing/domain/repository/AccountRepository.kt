package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork

interface AccountRepository {

    suspend fun getClientAccount(token: String): ResultWork<ClientAccount, DataError>
}