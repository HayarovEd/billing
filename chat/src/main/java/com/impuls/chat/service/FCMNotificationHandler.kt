package com.impuls.chat.service

import android.content.Intent
import android.util.Log
import com.impuls.chat.domain.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

object FCMNotificationHandler: KoinComponent {
    private val notificationRepository: NotificationRepository by inject()
    fun handleNotification(data: Map<String, String>) {
        data.forEach { string, string1 ->
            Log.d("FCMService", "onMessageReceived str: $string")
            Log.d("FCMService", "onMessageReceived str1: $string1")
        }
        val title = data["title"] ?: data["header"] ?: "Новое сообщение"
        val body = data["body"] ?: data["text"] ?: ""
        val chatId = data["chatId"] ?: data["chat_id"]

        if (chatId != null) {
            notificationRepository.showNotification(
                title = title,
                message = body,
                chatId = chatId
            )
        }
    }

    fun handleNotification(intent: Intent?) {
        val data = intent?.extras?.let { bundle ->
            bundle.keySet().associateWith { bundle.getString(it) }.filterValues { it != null } as Map<String, String>
        } ?: emptyMap()

        handleNotification(data)
    }
}