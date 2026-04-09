package com.impuls.billing.domain.repository

import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.model.ResultLogin

interface AuthRepository {
    suspend fun login(username: String, password: String): ResultWork<ResultLogin, DataError>

}