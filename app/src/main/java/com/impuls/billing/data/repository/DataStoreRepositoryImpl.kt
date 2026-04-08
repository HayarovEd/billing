package com.impuls.billing.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.impuls.billing.domain.model.LastAuthData
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.LABEL_BEARER
import com.impuls.billing.domain.utils.LAST_EMAIL
import com.impuls.billing.domain.utils.LAST_PASSWORD
import com.impuls.billing.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {


    override suspend fun setLocalAuthorization(lastAuthData: LastAuthData): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings[FIELD_LAST_EMAIL] = lastAuthData.login
                settings[FIELD_LAST_PASSWORD] = lastAuthData.password
            }
        }
    }

    override fun getLocalAuthorization(): Flow<ResultWork<LastAuthData, DataError.DataStore>> {
        return handleReadFlow {
            dataStore.data.map {
                LastAuthData(
                    login = it[FIELD_LAST_EMAIL] ?: "",
                    password = it[FIELD_LAST_PASSWORD] ?: "",
                )
            }
        }
    }


    override suspend fun readStateLocalAuthorization(): ResultWork<LastAuthData, DataError.DataStore> {
        return handleRead {
            LastAuthData(
                login = dataStore.data.first()[FIELD_LAST_EMAIL] ?: "",
                password = dataStore.data.first()[FIELD_LAST_PASSWORD] ?: "",
            )
        }
    }

    override suspend fun saveBearer(
        bearer: String
    ) {
        dataStore.edit { preferences ->
            preferences[FIELD_LABEL_BEARER] = bearer
        }
    }


    override suspend fun getBearer(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[FIELD_LABEL_BEARER]
        }
    }

    override suspend fun removeBearer() {
        dataStore.edit { preferences ->
            preferences.remove(FIELD_LABEL_BEARER)
        }
    }


    companion object {
        val FIELD_LAST_EMAIL = stringPreferencesKey(LAST_EMAIL)
        val FIELD_LAST_PASSWORD = stringPreferencesKey(LAST_PASSWORD)
        val FIELD_LABEL_BEARER = stringPreferencesKey(LABEL_BEARER)

    }
}