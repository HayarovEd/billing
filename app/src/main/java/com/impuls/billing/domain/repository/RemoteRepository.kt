package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.model.ResultLogin

interface RemoteRepository {
    suspend fun login(username: String, password: String): ResultWork<ResultLogin, DataError>
    suspend fun getClientAccount(): ResultWork<ClientAccount, DataError>
}