package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork

interface RemoteChatRepository {
    suspend fun onLoginFromApp(
        name: String,
        username: String,
        email: String,
        password: String
    ): ResultWork<Token, ChatDataError>

    suspend fun onRefresh(refresh: String): ResultWork<Token, ChatDataError>
    suspend fun getGroupByGroupName(
        accessToken: String,
        groupName: String
    ): ResultWork<Group, ChatDataError>

    suspend fun createChat(
        accessToken: String,
        groupId: String,
        name: String,
        phone: String,
        message: String,
        email: String
    ): ResultWork<Chat, ChatDataError>

    suspend fun getOpenChatsByUser(
        accessToken: String,
        isMy: Boolean = true,
        isOpen: Boolean = true
    ): ResultWork<List<Chat>, ChatDataError>

    suspend fun sendMessage(
        accessToken: String,
        message: String,
        chatId: String
    ): ResultWork<Unit, ChatDataError>

    suspend fun closeChat(
        accessToken: String,
        chatId: String
    ): ResultWork<Unit, ChatDataError>

    suspend fun getChatById(accessToken: String, chatId: String): ResultWork<Chat, ChatDataError>
}