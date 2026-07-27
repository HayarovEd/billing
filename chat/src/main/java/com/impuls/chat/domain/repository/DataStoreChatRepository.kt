package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.PushProvider
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.model.UserBillingData
import kotlinx.coroutines.flow.Flow

interface DataStoreChatRepository {
    suspend fun saveTokens(token: Token)
    suspend fun getToken(): Flow<Token?>
    suspend fun removeToken()
    suspend fun getStateToken(): Token?
    suspend fun savePushToken(token: String)
    suspend fun getPushToken(): String?
    suspend fun isSentPushToken(isSend: Boolean)
    suspend fun getSentPushToken(): Boolean
    suspend fun saveUserBillingData(
        name: String,
        login: String,
        password: String,
        email: String,
        billingToken: String,
    )

    suspend fun getUserBillingData(): UserBillingData?
    suspend fun saveTypePushProvider(provider: PushProvider)
    suspend fun getTypePushProvider(): PushProvider
}