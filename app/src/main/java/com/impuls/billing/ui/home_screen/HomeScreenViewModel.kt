package com.impuls.billing.ui.home_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.repository.DataStoreRepository
import com.impuls.billing.domain.repository.JwtRepository
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asUiText
import com.impuls.billing.ui.uikit.model.NavigationRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.time.Clock

class HomeScreenViewModel(
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val agrmId = savedStateHandle.toRoute<NavigationRoute.HomeRoute>().agrmId

    private val _state = MutableStateFlow(HomeScreenState(agrmId = agrmId,))
    val state = _state
        .onStart {
            loadData(
                action = {
                    loadVGroups(it)
                },
                navigate = {
                    viewModelScope.launch {
                        eventChannel.send(HomeScreenEvent.NavigateEvent)
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeScreenState(agrmId = agrmId,)
        )

    private val eventChannel = Channel<HomeScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.SelectVgroup -> {
                _state.value.copy(
                    selectedVGroupId = action.vgId,
                )
                    .updateState()
            }
        }
    }





    private fun loadVGroups(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.getVGroups(
                token=token,
                agrmId = agrmId
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                    )
                        .updateState()
                    eventChannel.send(HomeScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        vGroups = result.data,
                    )
                        .updateState()
                    if (result.data.isNotEmpty()) {
                        _state.value.copy(
                            selectedVGroupId = result.data.first().vgId
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    private fun loadData(
        action: (String) -> Unit,
        navigate: () -> Unit
    ) {
        viewModelScope.launch {
            dataStoreRepository.getBearer().collect { token ->
                if (token != null) {
                    val expired = jwtRepository.decodeJwt(token)?.expired
                    expired?.let { exp ->
                        if (exp < Clock.System.now().epochSeconds) {
                            dataStoreRepository.removeBearer()
                            navigate()
                        } else {
                            action(token)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        navigate()
                    }
                }
            }
        }
    }

    private fun HomeScreenState.updateState() {
        _state.update {
            this
        }
    }
}