package com.impuls.billing.domain.repository


import com.impuls.billing.domain.model.LastAuthData
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setLocalAuthorization(lastAuthData: LastAuthData): ResultWork<Unit, DataError.DataStore>
    fun getLocalAuthorization(): Flow<ResultWork<LastAuthData, DataError.DataStore>>
    suspend fun readStateLocalAuthorization(): ResultWork<LastAuthData, DataError.DataStore>
    suspend fun removeBearer()
    suspend fun getBearer(): Flow<String?>
    suspend fun saveBearer(bearer: String)
}