package com.impuls.chat.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.impuls.chat.domain.repository.DataStoreChatRepository
import com.impuls.chat.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FCMService : FirebaseMessagingService(), KoinComponent {

    private val dataStoreRepository: DataStoreChatRepository by inject()
    private val notificationRepository: NotificationRepository by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Deprecated("Deprecated in Java", ReplaceWith("onNewToken(token)"))
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        Log.d("FCMService", "onNewToken: $token")
        serviceScope.launch {
            dataStoreRepository.saveFcmToken(token)
        }
    }

    override fun onRegistered(installationId: String) {
        Log.d("FCMService", "Registered installation ID: $installationId")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCMService", "onMessageReceived: ${message.data}")
        
        val title = message.notification?.title ?: message.data["title"] ?: message.data["header"] ?: "Новое сообщение"
        val body = message.notification?.body ?: message.data["body"] ?: message.data["text"] ?: ""
        val chatId = message.data["chatId"] ?: message.data["chat_id"]

        notificationRepository.showNotification(
            title = title,
            message = body,
            chatId = chatId
        )
    }
}
