package com.impuls.chat.domain.repository

import android.util.Log
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
                    Log.e("TAG222", "empty token: ${resultLogin.error}")
                    onError(resultLogin.error)
                }

                is ResultWork.Success -> {
                    dataStoreRepository.saveTokens(resultLogin.data)
                    withContext(Dispatchers.IO) { function(resultLogin.data.access) }
                    withContext(Dispatchers.IO) { sendFCMToken(resultLogin.data.access) }
                }
            }
        } else {
            val access = JwtRepository.decodeJwt(token.access)
            if (access != null && access.expired > Clock.System.now().epochSeconds) {
                withContext(Dispatchers.IO) { function(token.access) }
                withContext(Dispatchers.IO) { sendFCMToken(token.access) }
            } else {
                when (val resultRefresh = repository.onRefresh(token.refresh)) {
                    is ResultWork.Error -> {
                        Log.e("TAG222", "refresh token: ${resultRefresh.error}")
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
                                Log.e("TAG222", "error token: ${resultLogin.error}")
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
                        withContext(Dispatchers.IO) { sendFCMToken(resultRefresh.data.access) }
                    }
                }
            }
        }
    }

    private suspend fun sendFCMToken(
        accessToken: String,
    ) {
        if (!dataStoreRepository.getSentFcmToken()) {
            val fcmToken = dataStoreRepository.getFcmToken()
            fcmToken?.let {
                when (repository.registerFCMToken(
                    fcmToken = it,
                    accessToken = accessToken,
                    deviceData = serviceChatRepository.getDeviceData(),
                )) {
                    is ResultWork.Error -> {
                        ///
                    }

                    is ResultWork.Success -> {
                        dataStoreRepository.isSentFcmToken(true)
                    }
                }
            }

        }
    }

    private suspend fun getUserBillingData(): UserBillingData? {
        return dataStoreRepository.getUserBillingData()
    }
}