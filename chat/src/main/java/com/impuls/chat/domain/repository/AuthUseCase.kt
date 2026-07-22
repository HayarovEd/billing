package com.impuls.chat.domain.repository

import android.util.Log
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.JwtRepository
import com.impuls.chat.domain.utils.ResultWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Clock


class AuthUseCase(
    private val repository: RemoteChatRepository,
    private val dataStoreRepository: DataStoreChatRepository,
) {
    suspend operator fun <T> invoke(
        function: suspend (accessToken: String) -> T,
        onError: suspend (error: ChatDataError) -> Unit,
        name: String,
        username: String,
        email: String,
        password: String,
        billingToken: String,
    ) {
        val token = dataStoreRepository.getStateToken()
        if (token == null) {
            when (val resultLogin = repository.onLoginFromApp(
                name = name,
                username = username,
                email = email,
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
                        when (val resultLogin = repository.onLoginFromApp(
                            name = name,
                            username = username,
                            email = email,
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
                    accessToken = accessToken
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
}