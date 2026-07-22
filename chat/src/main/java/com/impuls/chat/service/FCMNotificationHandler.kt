package com.impuls.chat.service

import android.content.Intent
import android.os.Bundle
import com.impuls.chat.domain.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object FCMNotificationHandler : KoinComponent {
    private val notificationRepository: NotificationRepository by inject()

    private const val KEY_TITLE = "title"
    private const val KEY_HEADER = "header"
    private const val KEY_BODY = "body"
    private const val KEY_TEXT = "text"
    private const val KEY_CHAT_ID = "chatId"
    private const val KEY_CHAT_ID_ALT = "chat_id"

    /**
     * Parses data and shows a system notification.
     * Used when a new message is received in [FCMService].
     */
    fun handleNotification(defaultHeader: String, data: Map<String, String>): FcmMessageData {
        val parsedData = parseMap(data, defaultHeader)
        
        if (parsedData.chatId != null) {
            notificationRepository.showNotification(
                title = parsedData.title,
                message = parsedData.body,
                chatId = parsedData.chatId
            )
        }
        
        return FcmMessageData(chatId = parsedData.chatId)
    }

    /**
     * Parses data from an Intent without showing a notification.
     * Used in UI components to react to a notification click.
     */
    fun handleIntent(defaultHeader: String, intent: Intent?): FcmMessageData {
        val bundle = intent?.extras ?: Bundle.EMPTY
        val data = bundle.keySet().associateWith { bundle.getString(it) }
        
        val parsedData = parseMap(data, defaultHeader)
        return FcmMessageData(chatId = parsedData.chatId)
    }

    private fun parseMap(data: Map<String, String?>, defaultHeader: String): ParsedFcmData {
        val title = data[KEY_TITLE] ?: data[KEY_HEADER] ?: defaultHeader
        val body = data[KEY_BODY] ?: data[KEY_TEXT] ?: ""
        val chatId = data[KEY_CHAT_ID] ?: data[KEY_CHAT_ID_ALT]
        
        return ParsedFcmData(title, body, chatId)
    }

    private data class ParsedFcmData(
        val title: String,
        val body: String,
        val chatId: String?
    )
}
