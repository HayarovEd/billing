package com.impuls.chat.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.model.UserBillingData
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"
private const val FCM_TOKEN = "fcm_token"
private const val SEND_FCM_TOKEN = "send_fcm_token"
private const val BILLING_NAME = "billing_name"
private const val BILLING_LOGIN = "billing_login"
private const val BILLING_PASSWORD = "billing_password"
private const val BILLING_EMAIL = "billing_email"
private const val BILLING_TOKEN = "billing_token"


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
        return ds[FIELD_SEND_FCM_TOKEN] ?: false
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

    override suspend fun saveUserBillingData(
        name: String,
        login: String,
        password: String,
        email: String,
        billingToken: String,
    ) {
        dataStore.edit { preferences ->
            preferences[FIELD_BILLING_NAME] = name
            preferences[FIELD_BILLING_LOGIN] = login
            preferences[FIELD_BILLING_PASSWORD] = password
            preferences[FIELD_BILLING_EMAIL] = email
            preferences[FIELD_BILLING_TOKEN] = billingToken
        }
    }

    override suspend fun getUserBillingData(): UserBillingData? {
        val ds = dataStore.data.first()
        val billingName = ds[FIELD_BILLING_NAME]
        val billingLogin = ds[FIELD_BILLING_LOGIN]
        val billingPassword = ds[FIELD_BILLING_PASSWORD]
        val billingEmail = ds[FIELD_BILLING_EMAIL]
        val billingToken = ds[FIELD_BILLING_TOKEN]
        return if (billingName != null
            && billingLogin != null
            && billingPassword != null
            && billingEmail != null
            && billingToken != null
        ) {
            UserBillingData(
                name = billingName,
                login = billingLogin,
                password = billingPassword,
                email = billingEmail,
                billingToken = billingToken,
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
        val FIELD_BILLING_NAME = stringPreferencesKey(BILLING_NAME)
        val FIELD_BILLING_LOGIN = stringPreferencesKey(BILLING_LOGIN)
        val FIELD_BILLING_PASSWORD = stringPreferencesKey(BILLING_PASSWORD)
        val FIELD_BILLING_EMAIL = stringPreferencesKey(BILLING_EMAIL)
        val FIELD_BILLING_TOKEN = stringPreferencesKey(BILLING_TOKEN)
    }
}