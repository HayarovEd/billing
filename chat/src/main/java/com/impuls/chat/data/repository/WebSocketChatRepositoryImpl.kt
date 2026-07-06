package com.impuls.chat.data.repository

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
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

internal class WebSocketChatRepositoryImpl(
    private val client: HttpClient,
) : WebSocketChatRepository {
    private var session: WebSocketSession? = null

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
        return flow<ResultWork<WebSocketChatMessage, ChatDataError.WebSocketErrorChat>> {
            session = client.webSocketSession {
                url("$WEB_SOCKET_URL/chats/$chatId/messages")
                //parameter("token", accessToken)
            }
            session?.let { sn ->
                sn.incoming
                    .consumeAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .collect { collector ->
                        val baseMessage = collector.readText()
                        when {
                            baseMessage.contains(PATH_CHAT) -> {
                                if (baseMessage.contains(UPDATED)) {
                                    val result =
                                        json.decodeFromString<UpdateChatDto<ChatDto>>(baseMessage)
                                    emit(
                                        ResultWork.Success(
                                            WebSocketChatMessage.UpdateChatMessage(
                                                result.remoteData.convertToChat()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
            }
        }.catch { e ->
            print(e.message)
            emit(ResultWork.Error(ChatDataError.WebSocketErrorChat.NOT_CONNECT))
        }
    }


    override suspend fun close() {
        session?.close()
        session = null
    }
}