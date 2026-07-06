package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.WebSocketChatMessage
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface WebSocketChatRepository {
    fun updateLiveChat(
        accessToken: String,
        chatId: String
    ): Flow<ResultWork<WebSocketChatMessage, ChatDataError.WebSocketErrorChat>>

    suspend fun close()
}