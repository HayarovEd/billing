package com.impuls.chat.data.repository

import android.util.Log
import com.impuls.chat.data.handler.handleResponse
import com.impuls.chat.data.mapper.convertToChat
import com.impuls.chat.data.mapper.convertToSubGroups
import com.impuls.chat.data.remote.request.CreateChatRequest
import com.impuls.chat.data.remote.request.LoginFromAppRequest
import com.impuls.chat.data.remote.request.MessageRequest
import com.impuls.chat.data.remote.response.GroupDto
import com.impuls.chat.data.remote.response.LoginFromAppDto
import com.impuls.chat.data.remote.response.create_chat_dto.ChatDto
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.SubGroup
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.repository.RemoteChatRepository
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.setCookie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RemoteRepositoryImpl(
    private val client: HttpClient,
) : RemoteChatRepository {


    override suspend fun onLoginFromApp(
        name: String,
        username: String,
        email: String,
        password: String
    ): ResultWork<Token, ChatDataError> {
        return withContext(Dispatchers.IO) {
            Log.d("TAG", "onLoginFromApp: name=$name, username=$username")
            handleResponse(
                request = {
                    client.post("auth/login_from_app") {
                        contentType(ContentType.Application.Json)
                        setBody(
                            LoginFromAppRequest(
                                name = name,
                                username = username,
                                email = email,
                                password = password
                            )
                        )
                    }
                },
                data = {
                    val refresh = extractRefreshTokenFromResponse(it)
                    val body = it.body<LoginFromAppDto>()
                    Token(
                        access = body.token,
                        refresh = refresh ?: ""
                    )
                }
            )
        }
    }

    override suspend fun onRefresh(refresh: String): ResultWork<Token, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("refresh") {
                        contentType(ContentType.Application.Json)
                        cookie("refreshToken", refresh)
                    }
                },
                data = {
                    val refresh = extractRefreshTokenFromResponse(it)
                    val body = it.body<LoginFromAppDto>()
                    Token(
                        access = body.token,
                        refresh = refresh ?: ""
                    )
                }
            )
        }
    }

    override suspend fun getThemesByGroupName(
        accessToken: String,
        groupName: String,
    ): ResultWork<List<SubGroup>, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("groups/") {
                        contentType(ContentType.Application.Json)
                        parameter("name", groupName)
                        bearerAuth(accessToken)
                    }
                },
                data = {
                    it.body<GroupDto>().convertToSubGroups()
                }
            )
        }
    }

    override suspend fun createChat(
        accessToken: String,
        subGroupId: String,
        name: String,
        phone: String,
        message: String,
        email: String
    ): ResultWork<Chat, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("chats") {
                        contentType(ContentType.Application.Json)
                        parameter("subgroupId", subGroupId)
                        bearerAuth(accessToken)
                        setBody(
                            CreateChatRequest(
                                name = name,
                                phone = phone,
                                message = message,
                                email = email
                            )
                        )
                    }
                },
                data = {
                    it.body<ChatDto>().convertToChat()
                }
            )
        }
    }

    override suspend fun getOpenChatsByUser(
        accessToken: String,
        isMy: Boolean,
        isOpen: Boolean
    ): ResultWork<List<Chat>, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("chats") {
                        contentType(ContentType.Application.Json)
                        parameter("isMy", isMy)
                        parameter("isOpen", isOpen)
                        bearerAuth(accessToken)
                    }
                },
                data = {
                   val rs = it.bodyAsText()
                    print(rs)
                    it.body<List<ChatDto>>().map { ct ->
                        ct.convertToChat()
                    }
                }
            )
        }
    }


    override suspend fun getChatById(
        accessToken: String,
        chatId: String
    ): ResultWork<Chat, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("chats/$chatId") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                    }
                },
                data = {
                    it.body<ChatDto>().convertToChat()
                }
            )
        }
    }

    override suspend fun sendMessage(
        accessToken: String,
        message: String,
        chatId: String
    ): ResultWork<Unit, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("chats/$chatId/message/client") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                        setBody(
                            MessageRequest(
                                text = message
                            )
                        )
                    }
                },
                data = {
                }
            )
        }
    }

    override suspend fun closeChat(
        accessToken: String,
        chatId: String
    ): ResultWork<Unit, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("chats/$chatId/close") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                    }
                },
                data = {}
            )
        }
    }

    private fun extractRefreshTokenFromResponse(response: HttpResponse): String? {
        return response.setCookie()
            .find { it.name.equals("refreshToken", ignoreCase = true) }
            ?.value
    }
}