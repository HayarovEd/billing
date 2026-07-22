package com.impuls.chat.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"
private const val FCM_TOKEN = "fcm_token"
private const val SEND_FCM_TOKEN = "send_fcm_token"



internal class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : DataStoreChatRepository {


    override suspend fun saveTokens(
        token: Token
    ) {
        dataStore.edit { preferences ->
            preferences[FIELD_ACCESS_TOKEN] = token.access
            preferences[FIELD_REFRESH_TOKEN] = token.refresh
        }
    }

    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[FIELD_FCM_TOKEN] = token
        }
    }

    override suspend fun getFcmToken(): String? {
        val ds = dataStore.data.first()
        return ds[FIELD_FCM_TOKEN]
    }

    override suspend fun isSentFcmToken(isSend: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIELD_SEND_FCM_TOKEN] = isSend
        }
    }

    override suspend fun getSentFcmToken(): Boolean {
        val ds = dataStore.data.first()
        return ds[FIELD_SEND_FCM_TOKEN]?:false
    }


    override suspend fun getToken(): Flow<Token?> {
        return dataStore.data.map { preferences ->
            val accessToken = preferences[FIELD_ACCESS_TOKEN]
            val refreshToken = preferences[FIELD_REFRESH_TOKEN]
            if (accessToken != null && refreshToken != null) {
                Token(
                    access = accessToken,
                    refresh = refreshToken
                )
            } else {
                null
            }
        }
    }

    override suspend fun getStateToken(): Token? {
        val ds = dataStore.data.first()
        val accessToken = ds[FIELD_ACCESS_TOKEN]
        val refreshToken = ds[FIELD_REFRESH_TOKEN]
        return if (accessToken != null && refreshToken != null) {
            Token(
                access = accessToken,
                refresh = refreshToken
            )
        } else null
    }

    override suspend fun removeToken() {
        dataStore.edit { preferences ->
            preferences.remove(FIELD_ACCESS_TOKEN)
            preferences.remove(FIELD_REFRESH_TOKEN)
        }
    }


    companion object {
        val FIELD_ACCESS_TOKEN = stringPreferencesKey(ACCESS_TOKEN)
        val FIELD_REFRESH_TOKEN = stringPreferencesKey(REFRESH_TOKEN)
        val FIELD_FCM_TOKEN = stringPreferencesKey(FCM_TOKEN)
        val FIELD_SEND_FCM_TOKEN = booleanPreferencesKey(SEND_FCM_TOKEN)
    }
}