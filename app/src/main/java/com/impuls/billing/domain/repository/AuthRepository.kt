package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.RecoverPassword
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.model.ResultLogin

interface AuthRepository {
    suspend fun login(username: String, password: String): ResultWork<ResultLogin, DataError>

    suspend fun recoverPassword(
        login: String,
        transport: Int
    ): ResultWork<RecoverPassword, DataError>

    suspend fun updatePasswordByCode(
        login: String,
        password: String,
        code: String
    ): ResultWork<Boolean, DataError>
}