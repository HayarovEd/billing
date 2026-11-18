package com.impuls.billing.ui.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

class SplashScreenViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository
) : ViewModel() {

    private val eventChannel = Channel<SplashScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(2000)
            dataStoreRepository.getBearer().collect { userToken ->
                if (!userToken.isNullOrBlank()) {
                    val expired = jwtRepository.decodeJwt(userToken)?.expired
                    if (expired == null || expired < Clock.System.now().epochSeconds) {
                        dataStoreRepository.removeBearer()
                        eventChannel.send(SplashScreenEvent.LoginNavigateEvent)
                    } else {
                        eventChannel.send(SplashScreenEvent.ProfileNavigateEvent)
                    }
                } else {
                    dataStoreRepository.removeBearer()
                    eventChannel.send(SplashScreenEvent.LoginNavigateEvent)
                }
            }
        }
    }
}
