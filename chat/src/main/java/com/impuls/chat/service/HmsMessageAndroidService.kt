package com.impuls.chat.service

import android.os.Bundle
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.impuls.chat.domain.model.PushProvider
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
            data = message.dataOfMap
        )
    }

    override fun onNewToken(token: String?) {
        saveToken(token)
    }

    override fun onNewToken(token: String?, p1: Bundle?) {
        saveToken(token)
    }
    
    private fun saveToken(token: String?) {
        token?.let {
            serviceScope.launch {
                dataStoreRepository.savePushToken(it)
                dataStoreRepository.saveTypePushProvider(PushProvider.HMS)
                dataStoreRepository.isSentPushToken(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}