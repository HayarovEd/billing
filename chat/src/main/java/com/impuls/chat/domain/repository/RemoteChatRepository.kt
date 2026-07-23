package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.DeviceData
import com.impuls.chat.domain.model.DownloadType
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.PLATFORM_FCM
import com.impuls.chat.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import java.io.File

interface RemoteChatRepository {
    suspend fun onLoginFromApp(
        name: String,
        username: String,
        email: String,
        password: String,
        token: String,
    ): ResultWork<Token, ChatDataError>

    suspend fun onRefresh(refresh: String): ResultWork<Token, ChatDataError>
    suspend fun getGroupByOperId(
        accessToken: String,
        operId: Long
    ): ResultWork<Group, ChatDataError>

    suspend fun createChat(
        accessToken: String,
        groupId: String,
        name: String,
        phone: String,
        message: String,
        email: String,
        login: String,
    ): ResultWork<Chat, ChatDataError>

    suspend fun getOpenChatsByUser(
        accessToken: String,
        login: String,
    ): ResultWork<List<Chat>, ChatDataError>

    suspend fun sendMessage(
        accessToken: String,
        message: String?,
        chatId: String,
        files: List<File>?
    ): ResultWork<Unit, ChatDataError>

    suspend fun closeChat(
        accessToken: String,
        chatId: String
    ): ResultWork<Unit, ChatDataError>

    suspend fun getChatById(accessToken: String, chatId: String): ResultWork<Chat, ChatDataError>
    suspend fun markMessageAsRead(
        accessToken: String,
        messageId: String
    ): ResultWork<Unit, ChatDataError>

    suspend fun downloadFile(accessToken: String, fileName: String): Flow<DownloadType>
    suspend fun registerFCMToken(
        fcmToken: String,
        accessToken: String,
        deviceData: DeviceData,
        platform: String = PLATFORM_FCM,
    ): ResultWork<Unit, ChatDataError>
}