package com.impuls.chat.data.repository

import android.util.Log
import com.impuls.chat.data.mapper.convertToChat
import com.impuls.chat.data.remote.response.create_chat_dto.ChatDto
import com.impuls.chat.data.remote.websocket.UpdateChatDto
import com.impuls.chat.domain.model.WebSocketChatMessage
import com.impuls.chat.domain.repository.WebSocketChatRepository
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.PATH_CHAT
import com.impuls.chat.domain.utils.ResultWork
import com.impuls.chat.domain.utils.UPDATED
import com.impuls.chat.domain.utils.WEB_SOCKET_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds

internal class WebSocketChatRepositoryImpl(
    private val client: HttpClient,
) : WebSocketChatRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        explicitNulls = false
    }

    override fun updateLiveChat(
        accessToken: String,
        chatId: String
    ): Flow<ResultWork<WebSocketChatMessage, ChatDataError.WebSocketErrorChat>> {
        return flow {
            while (currentCoroutineContext().isActive) {
                try {
                    val session = client.webSocketSession {
                        url("$WEB_SOCKET_URL/chats/$chatId/messages")
                    }
                    
                    try {
                        session.incoming
                            .receiveAsFlow()
                            .filterIsInstance<Frame.Text>()
                            .collect { collector ->
                                val baseMessage = collector.readText()
                                if (baseMessage.contains(PATH_CHAT) && baseMessage.contains(UPDATED)) {
                                    val result = json.decodeFromString<UpdateChatDto<ChatDto>>(baseMessage)
                                    emit(
                                        ResultWork.Success(
                                            WebSocketChatMessage.UpdateChatMessage(
                                                result.remoteData.convertToChat()
                                            )
                                        )
                                    )
                                }
                            }
                    } finally {
                        session.close()
                    }
                } catch (e: Exception) {
                    if (!currentCoroutineContext().isActive) throw e
                    Log.e("WS_CHAT", "Connection failed: ${e.message}")
                    emit(ResultWork.Error(ChatDataError.WebSocketErrorChat.NOT_CONNECT))
                }
                
                // Wait before reconnecting
                delay(RECONNECT_DELAY.milliseconds)
            }
        }
    }

    companion object {
        private const val RECONNECT_DELAY = 5000L
    }
}