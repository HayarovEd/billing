package com.impuls.billing.ui.blocking_screen

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

class BlockingScreenViewModel(
    private val accountRepository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val jwtRepository: JwtRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val vgId = savedStateHandle.toRoute<NavigationRoute.BlockingRoute>().vgId

    private val _state = MutableStateFlow(BlockingScreenState())
    val state = _state
        .onStart {
            loadData(
                action = {
                    loadVGroups(it)
                },
                navigate = {
                    viewModelScope.launch {
                        eventChannel.send(BlockingScreenEvent.NavigateEvent)
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = BlockingScreenState()
        )

    private val eventChannel = Channel<BlockingScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: BlockingScreenAction) {
        when (action) {
            is BlockingScreenAction.UpdateSelectedDate -> {
                _state.value.copy(
                    selectedDate = action.date,
                )
                    .updateState()
            }
        }
    }

    private fun loadVGroups(token: String) {
        viewModelScope.launch {
            when (val result = accountRepository.getVGroups(
                token = token,
                agrmId = null,
                vgId = vgId
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                    )
                        .updateState()
                    eventChannel.send(BlockingScreenEvent.OnMessage(result.error.asUiText()))
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        vGroups = result.data,
                    )
                        .updateState()
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

    private fun BlockingScreenState.updateState() {
        _state.update {
            this
        }
    }
}