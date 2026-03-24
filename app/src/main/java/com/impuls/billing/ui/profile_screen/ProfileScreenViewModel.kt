package com.impuls.billing.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.RemoteRepository
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel

class ProfileScreenViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {



    private val _state = MutableStateFlow(ProfileScreenState())
    val state = _state
        .onStart {
            loadAccountData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileScreenState()
        )

    private val eventChannel = Channel<ProfileScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ProfileScreenAction) {
        when (action) {
            else -> {
                // TODO("Handle actions")
                /*viewModelScope.launch {
                    eventChannel.send(
                        //TODO
                    )
                }*/
            }
        }
    }


    private fun loadAccountData() {
        _state.value.copy(
            isLoading = true,
        )
            .updateState()
        viewModelScope.launch {
            when(val result = remoteRepository.getClientAccount()) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                    )
                        .updateState()
                    eventChannel.send(ProfileScreenEvent.OnMessage(result.error.asUiText()))
                }
                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        account = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private fun ProfileScreenState.updateState() {
        _state.update {
            this
        }
    }
}