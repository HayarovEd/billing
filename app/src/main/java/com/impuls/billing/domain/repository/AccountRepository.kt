package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.ClientInfo
import com.impuls.billing.domain.model.ClientNotices
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork

interface AccountRepository {

    suspend fun getClientAccount(token: String): ResultWork<ClientAccount, DataError>
    suspend fun setClientMobilePhone(
        token: String,
        uid: Long,
        phone: String
    ): ResultWork<Boolean, DataError>

    suspend fun validateClientMobileNumber(
        token: String,
    ): ResultWork<Long, DataError>

    suspend fun setClientEmail(
        token: String,
        uid: Long,
        email: String,
    ): ResultWork<Boolean, DataError>

    suspend fun validateClientEmail(token: String): ResultWork<Long, DataError>
    suspend fun confirmValidateClientMobileNumber(
        token: String,
        id: Long,
        code: String
    ): ResultWork<Boolean, DataError>

    suspend fun confirmValidateClientEmail(
        token: String,
        id: Long,
        code: String
    ): ResultWork<Boolean, DataError>

    suspend fun getVGroups(
        token: String,
        agrmId: Long?,
        vgId: Long? = null
    ): ResultWork<List<ClientVroup>, DataError>

    suspend fun getClientInfo(token: String): ResultWork<ClientInfo, DataError>
    suspend fun getClientNotices(token: String, userid: Long): ResultWork<ClientNotices, DataError>
    suspend fun updateClientNotices(
        token: String,
        clientNotices: ClientNotices
    ): ResultWork<Long, DataError>

    suspend fun updatePassword(
        token: String,
        oldPassword: String,
        newPassword: String
    ): ResultWork<Boolean, DataError>
}