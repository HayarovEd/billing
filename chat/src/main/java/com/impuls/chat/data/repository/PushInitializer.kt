package com.impuls.chat.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.huawei.hms.aaid.HmsInstanceId
import com.impuls.chat.domain.model.PushProvider
import com.impuls.chat.domain.repository.DataStoreChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

object PushInitializer : KoinComponent {

    private val dataStoreRepository: DataStoreChatRepository by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private const val TAG = "PushInitializer"

    fun init(context: Context) {
        val provider = PushAvailabilityChecker.getAvailableProvider(context)
        Log.d(TAG, "Selected push provider: $provider")
        savePushProvider(provider)

        when (provider) {
            PushProvider.FCM -> requestFcmToken()
            PushProvider.HMS -> requestHmsToken(context)
            PushProvider.NONE -> Log.e(TAG, "No push services available on this device")
        }
    }

    private fun requestFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            // Отправляем в наш единый обработчик (из предыдущего шага)
            saveToken(token)
        }
    }

    private fun requestHmsToken(context: Context) {
        // Запрос HMS токена должен происходить в фоновом потоке
        Thread {
            try {
                // Получаем appId из файла agconnect-services.json автоматически через SDK
                val appId = com.huawei.agconnect.config.AGConnectServicesConfig
                    .fromContext(context)
                    .getString("client/app_id")

                // Запрашиваем токен (HCM_SCOPES указывает на работу с пушами)
                val token = HmsInstanceId.getInstance(context).getToken(appId, "HCM")

                if (!token.isNullOrEmpty()) {
                    // Передаем токен в единый обработчик
                    saveToken(token, )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fetching HMS registration token failed", e)
            }
        }.start()
    }

    private fun saveToken(token: String) {
        serviceScope.launch {
            dataStoreRepository.saveFcmToken(token)
            dataStoreRepository.isSentFcmToken(false)
        }
    }

    private fun savePushProvider(provider: PushProvider) {
        serviceScope.launch {
            dataStoreRepository.saveTypePushProvider(provider)
        }
    }
}