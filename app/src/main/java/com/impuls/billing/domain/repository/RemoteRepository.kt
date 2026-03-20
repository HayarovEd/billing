package com.impuls.billing.domain.repository

import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork

interface RemoteRepository {
    suspend fun login(username: String, password: String): ResultWork<Unit, DataError>
}