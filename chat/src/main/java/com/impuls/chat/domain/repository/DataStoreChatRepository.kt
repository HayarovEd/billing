package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.Token
import kotlinx.coroutines.flow.Flow

interface DataStoreChatRepository {
    suspend fun saveTokens(token: Token)
    suspend fun getToken(): Flow<Token?>
    suspend fun removeToken()
    suspend fun getStateToken(): Token?
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String?
}