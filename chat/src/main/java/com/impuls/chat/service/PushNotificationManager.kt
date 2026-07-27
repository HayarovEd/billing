package com.impuls.chat.service

import android.content.Intent
import android.os.Bundle
import com.impuls.chat.domain.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PushNotificationManager : KoinComponent {
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
    fun handleNotification(
        defaultHeader: String,
        data: Map<String, String>,
        title: String,
        body: String
    ): PushMessageData {
        val parsedData = parseMap(data, defaultHeader)
        
        // Fallback for Data Messages if title/body are empty
        val finalTitle = title.ifEmpty { parsedData.title }
        val finalBody = body.ifEmpty { parsedData.body }
        
        if (parsedData.chatId != null) {
            notificationRepository.showNotification(
                title = finalTitle,
                message = finalBody,
                chatId = parsedData.chatId,
                data = data
            )
        }
        
        return PushMessageData(chatId = parsedData.chatId)
    }

    /**
     * Parses data from an Intent without showing a notification.
     * Used in UI components to react to a notification click.
     */
    fun handleIntent(defaultHeader: String, intent: Intent?): PushMessageData {
        val bundle = intent?.extras ?: Bundle.EMPTY
        val data = bundle.keySet().associateWith { bundle.getString(it) }
        
        val parsedData = parseMap(data, defaultHeader)
        return PushMessageData(chatId = parsedData.chatId)
    }

    private fun parseMap(data: Map<String, String?>, defaultHeader: String): ParsedPushData {
        val title = data[KEY_TITLE] ?: data[KEY_HEADER] ?: defaultHeader
        val body = data[KEY_BODY] ?: data[KEY_TEXT] ?: ""
        val chatId = data[KEY_CHAT_ID] ?: data[KEY_CHAT_ID_ALT]
        
        return ParsedPushData(title, body, chatId)
    }

    private data class ParsedPushData(
        val title: String,
        val body: String,
        val chatId: String?
    )
}
