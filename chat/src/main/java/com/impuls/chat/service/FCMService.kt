package com.impuls.chat.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FCMService : FirebaseMessagingService(), KoinComponent {

    private val dataStoreRepository: DataStoreChatRepository by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Deprecated("Deprecated in Java", ReplaceWith("onNewToken(token)"))
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        serviceScope.launch {
            dataStoreRepository.savePushToken(token)
            dataStoreRepository.saveTypePushProvider(com.impuls.chat.domain.model.PushProvider.FCM)
            dataStoreRepository.isSentPushToken(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onRegistered(installationId: String) {
        Log.d("FCMService", "Registered installation ID: $installationId")
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        PushNotificationManager.handleNotification(
            defaultHeader = resources.getString(com.impuls.chat.R.string.new_message),
            title = message.notification?.title ?: "",
            body = message.notification?.body ?: "",
            data = message.data
        )
    }
}
