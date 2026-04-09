package com.impuls.billing.domain.repository

import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.model.ResultLogin

interface AuthRepository {
    suspend fun login(username: String, password: String): ResultWork<ResultLogin, DataError>
    suspend fun setClientMobilePhone(uid: Int, phone: String): ResultWork<Boolean, DataError>
    suspend fun validateClientMobileNumber(): ResultWork<Int, DataError>
    suspend fun setClientEmail(uid: Int, email: String): ResultWork<Boolean, DataError>
    suspend fun validateClientEmail(): ResultWork<Int, DataError>
    suspend fun confirmValidateClientMobileNumber(
        id: Int,
        code: String
    ): ResultWork<Boolean, DataError>

    suspend fun confirmValidateClientEmail(id: Int, code: String): ResultWork<Boolean, DataError>
}