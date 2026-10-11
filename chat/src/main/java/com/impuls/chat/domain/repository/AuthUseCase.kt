package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.PushProvider
import com.impuls.chat.domain.model.UserBillingData
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.JwtRepository
import com.impuls.chat.domain.utils.ResultWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Clock


class AuthUseCase(
    private val repository: RemoteChatRepository,
    private val dataStoreRepository: DataStoreChatRepository,
    private val serviceChatRepository: ServiceChatRepository,
) {
    suspend operator fun <T> invoke(
        function: suspend (accessToken: String) -> T,
        onError: suspend (error: ChatDataError) -> Unit,
        currentName: String? = null,
        currentEmail: String? = null,
    ) {
        val token = dataStoreRepository.getStateToken()
        if (token == null) {
            val userBillingData = getUserBillingData()
            if (userBillingData == null) {
                onError(ChatDataError.Network.UNKNOWN)
                return
            }
            val (name, username, password, email, billingToken) = userBillingData
            when (val resultLogin = repository.onLoginFromApp(
                name = currentName ?: name,
                username = username,
                email = currentEmail ?: email,
                password = password,
                token = billingToken,
            )) {
                is ResultWork.Error -> {
                    onError(resultLogin.error)
                }

                is ResultWork.Success -> {
                    dataStoreRepository.saveTokens(resultLogin.data)
                    withContext(Dispatchers.IO) { function(resultLogin.data.access) }
                    withContext(Dispatchers.IO) { sendPushToken(resultLogin.data.access) }
                }
            }
        } else {
            val access = JwtRepository.decodeJwt(token.access)
            if (access != null && access.expired > Clock.System.now().epochSeconds) {
                withContext(Dispatchers.IO) { function(token.access) }
                withContext(Dispatchers.IO) { sendPushToken(token.access) }
            } else {
                when (val resultRefresh = repository.onRefresh(token.refresh)) {
                    is ResultWork.Error -> {
                        onError(resultRefresh.error)
                        dataStoreRepository.removeToken()
                        val userBillingData = getUserBillingData()
                        if (userBillingData == null) {
                            onError(ChatDataError.Network.UNKNOWN)
                            return
                        }
                        val (name, username, password, email, billingToken) = userBillingData
                        when (val resultLogin = repository.onLoginFromApp(
                            name = currentName ?: name,
                            username = username,
                            email = currentEmail ?: email,
                            password = password,
                            token = billingToken,
                        )) {
                            is ResultWork.Error -> {
                                onError(resultLogin.error)
                            }

                            is ResultWork.Success -> {
                                dataStoreRepository.saveTokens(resultLogin.data)
                                function(resultLogin.data.access)
                            }
                        }
                    }

                    is ResultWork.Success -> {
                        dataStoreRepository.saveTokens(resultRefresh.data)
                        withContext(Dispatchers.IO) { function(resultRefresh.data.access) }
                        withContext(Dispatchers.IO) { sendPushToken(resultRefresh.data.access) }
                    }
                }
            }
        }
    }

    private suspend fun sendPushToken(
        accessToken: String,
    ) {
        if (!dataStoreRepository.getSentPushToken()) {
            val pushToken = dataStoreRepository.getPushToken()
            val typePushProvider = dataStoreRepository.getTypePushProvider()
            val platform = when (typePushProvider) {
                PushProvider.FCM -> "FCM"
                PushProvider.HMS -> "HMS"
                PushProvider.NONE -> "NONE"
            }
            if (typePushProvider != PushProvider.NONE) {
                pushToken?.let {
                    when (repository.registerPushToken(
                        pushToken = it,
                        accessToken = accessToken,
                        deviceData = serviceChatRepository.getDeviceData(),
                        platform = platform,
                    )) {
                        is ResultWork.Error -> {
                            ///
                        }

                        is ResultWork.Success -> {
                            dataStoreRepository.isSentPushToken(true)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getUserBillingData(): UserBillingData? {
        return dataStoreRepository.getUserBillingData()
    }
}