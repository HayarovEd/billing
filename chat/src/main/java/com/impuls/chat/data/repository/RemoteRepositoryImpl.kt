package com.impuls.chat.data.repository

import android.util.Log
import com.impuls.chat.data.handler.handleResponse
import com.impuls.chat.data.mapper.convertToChat
import com.impuls.chat.data.mapper.convertToGroup
import com.impuls.chat.data.remote.request.CreateChatRequest
import com.impuls.chat.data.remote.request.LoginFromAppRequest
import com.impuls.chat.data.remote.request.MessageRequest
import com.impuls.chat.data.remote.response.GroupDto
import com.impuls.chat.data.remote.response.LoginFromAppDto
import com.impuls.chat.data.remote.response.create_chat_dto.ChatDto
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.DownloadType
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.repository.RemoteChatRepository
import com.impuls.chat.domain.utils.BASE_CHAT_URL
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import com.impuls.chat.domain.utils.getMimeTypeExtended
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.setCookie
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

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
                    client.post("${BASE_CHAT_URL}auth/login_from_app") {
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
                    client.post("${BASE_CHAT_URL}refresh") {
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

    override suspend fun getGroupByOperId(
        accessToken: String,
        operId: Long,
    ): ResultWork<Group, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("${BASE_CHAT_URL}groups/oper") {
                        contentType(ContentType.Application.Json)
                        parameter("oper_id", operId)
                        bearerAuth(accessToken)
                    }
                },
                data = {
                    it.body<GroupDto>().convertToGroup()
                }
            )
        }
    }

    override suspend fun createChat(
        accessToken: String,
        groupId: String,
        name: String,
        phone: String,
        message: String,
        email: String,
        login: String,
    ): ResultWork<Chat, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("${BASE_CHAT_URL}chats") {
                        contentType(ContentType.Application.Json)
                        parameter("groupId", groupId)
                        bearerAuth(accessToken)
                        setBody(
                            CreateChatRequest(
                                name = name,
                                login = login,
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
        login: String,
    ): ResultWork<List<Chat>, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("${BASE_CHAT_URL}chats/user_chats") {
                        contentType(ContentType.Application.Json)
                        parameter("login", login)
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
                    client.get("${BASE_CHAT_URL}chats/$chatId") {
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
        message: String?,
        chatId: String,
        files: List<File>?
    ): ResultWork<Unit, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    val boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"
                    val parts = mutableListOf<ByteArray>()

                    // 1. Data часть
                    if (message!=null) {
                        val jsonData = Json.encodeToString(MessageRequest(message))
                        parts.add("--$boundary\r\n".toByteArray())
                        parts.add("Content-Disposition: form-data; name=\"data\"\r\n".toByteArray())
                        parts.add("Content-Type: application/json\r\n\r\n".toByteArray())
                        parts.add("$jsonData\r\n".toByteArray())
                    }

                    // 2. Files часть (всегда добавляем, даже пустую)
                    if (files.isNullOrEmpty()) {
                        parts.add("--$boundary\r\n".toByteArray())
                        parts.add("Content-Disposition: form-data; name=\"files\"\r\n".toByteArray())
                        parts.add("Content-Type: application/octet-stream\r\n\r\n".toByteArray())
                        parts.add("\r\n".toByteArray())
                    } else {
                        files.forEach { file ->
                            val fileBytes = file.readBytes()
                            Log.d("WebSocket message received", "file $file")
                            Log.d("WebSocket message received", "fileBytes ${fileBytes.size}")
                            parts.add("--$boundary\r\n".toByteArray())
                            parts.add("Content-Disposition: form-data; name=\"files\"; filename=\"${file.name}\"\r\n".toByteArray())
                            parts.add("Content-Type: ${getMimeTypeExtended(file)}\r\n\r\n".toByteArray())
                            parts.add(fileBytes)
                            parts.add("\r\n".toByteArray())
                        }
                    }

                    // 3. Закрывающий boundary
                    parts.add("--$boundary--\r\n".toByteArray())

                    // 4. Объединяем всё в один массив байт
                    val body = parts.reduce { acc, bytes -> acc + bytes }

                    // 5. Отправляем
                    client.post("${BASE_CHAT_URL}chats/$chatId/message/client") {
                        bearerAuth(accessToken)
                        header(HttpHeaders.ContentType, "multipart/form-data; boundary=$boundary")
                        header(HttpHeaders.ContentLength, body.size.toString())
                        setBody(body)
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
                    client.post("${BASE_CHAT_URL}chats/$chatId/close") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                    }
                },
                data = {}
            )
        }
    }

    override suspend fun markMessageAsRead(
        accessToken: String,
        messageId: String
    ): ResultWork<Unit, ChatDataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("${BASE_CHAT_URL}messages/$messageId/read") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                    }
                },
                data = {}
            )
        }
    }

    override suspend fun downloadFile(
        accessToken: String,
        fileName: String
    ): Flow<DownloadType> = channelFlow {
        send(DownloadType.Started)

        val bytesChannel = client.get(BASE_CHAT_URL + "files/" + fileName) {
            bearerAuth(accessToken)
            onDownload { bytesSentTotal, contentLength ->
                trySend(DownloadType.Progress(value = "$bytesSentTotal of $contentLength"))
            }
        }.bodyAsChannel()

        send(DownloadType.Finished(byteArray = bytesChannel.toByteArray()))
    }.catch { error ->
        error.printStackTrace()
        emit(DownloadType.Failed(error = ChatDataError.Network.DOWNLOAD_FILE_ERROR))
    }


    private fun extractRefreshTokenFromResponse(response: HttpResponse): String? {
        return response.setCookie()
            .find { it.name.equals("refreshToken", ignoreCase = true) }
            ?.value
    }
}