package com.impuls.chat.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.impuls.chat.domain.model.PushProvider
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.model.UserBillingData
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"
private const val PUSH_TOKEN = "fcm_token" // Keeping key "fcm_token" for backward compatibility
private const val SEND_PUSH_TOKEN = "send_fcm_token"
private const val BILLING_NAME = "billing_name"
private const val BILLING_LOGIN = "billing_login"
private const val BILLING_PASSWORD = "billing_password"
private const val BILLING_EMAIL = "billing_email"
private const val BILLING_TOKEN = "billing_token"
private const val PUSH_PROVIDER = "push_provider"

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

    override suspend fun savePushToken(token: String) {
        dataStore.edit { preferences ->
            preferences[FIELD_PUSH_TOKEN] = token
        }
    }

    override suspend fun getPushToken(): String? {
        val ds = dataStore.data.first()
        return ds[FIELD_PUSH_TOKEN]
    }

    override suspend fun isSentPushToken(isSend: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIELD_SEND_PUSH_TOKEN] = isSend
        }
    }

    override suspend fun getSentPushToken(): Boolean {
        val ds = dataStore.data.first()
        return ds[FIELD_SEND_PUSH_TOKEN] ?: false
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

    override suspend fun saveTypePushProvider(provider: PushProvider) {
        dataStore.edit { preferences ->
            preferences[FIELD_PUSH_PROVIDER] = when (provider) {
                PushProvider.FCM -> "FCM"
                PushProvider.HMS -> "HMS"
                PushProvider.NONE -> "NONE"
            }
        }
    }

    override suspend fun getTypePushProvider(): PushProvider {
        val ds = dataStore.data.first()
        return when (ds[FIELD_PUSH_PROVIDER]) {
            "FCM" -> PushProvider.FCM
            "HMS" -> PushProvider.HMS
            else -> PushProvider.NONE
        }
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
        val FIELD_PUSH_TOKEN = stringPreferencesKey(PUSH_TOKEN)
        val FIELD_SEND_PUSH_TOKEN = booleanPreferencesKey(SEND_PUSH_TOKEN)
        val FIELD_BILLING_NAME = stringPreferencesKey(BILLING_NAME)
        val FIELD_BILLING_LOGIN = stringPreferencesKey(BILLING_LOGIN)
        val FIELD_BILLING_PASSWORD = stringPreferencesKey(BILLING_PASSWORD)
        val FIELD_BILLING_EMAIL = stringPreferencesKey(BILLING_EMAIL)
        val FIELD_BILLING_TOKEN = stringPreferencesKey(BILLING_TOKEN)
        val FIELD_PUSH_PROVIDER = stringPreferencesKey(PUSH_PROVIDER)
    }
}