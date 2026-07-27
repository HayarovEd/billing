package com.impuls.chat.service

import android.os.Bundle
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class HmsMessageAndroidService: HmsMessageService(), KoinComponent {

    private val dataStoreRepository: DataStoreChatRepository by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        message ?: return
        PushNotificationManager.handleNotification(
            defaultHeader = resources.getString(com.impuls.chat.R.string.new_message),
            title = message.notification?.title ?: "",
            body = message.notification?.body ?: "",
            data = message.dataOfMap //data
        )
    }

    override fun onNewToken(token: String?) {
        token?.let {
            serviceScope.launch {
                dataStoreRepository.saveFcmToken(it)
                dataStoreRepository.isSentFcmToken(false)
            }
        }

    }

    override fun onNewToken(token: String?, p1: Bundle?) {
        token?.let {
            serviceScope.launch {
                dataStoreRepository.saveFcmToken(it)
                dataStoreRepository.isSentFcmToken(false)
            }
        }
    }
}