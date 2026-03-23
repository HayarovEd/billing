package com.impuls.billing.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.impuls.billing.domain.repository.RemoteRepository
import com.impuls.billing.domain.utils.ResultWork
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
        viewModelScope.launch {
            when(val result = remoteRepository.getClientAccount()) {
                is ResultWork.Error<*, *> -> {
                    println(result.error)
                }
                is ResultWork.Success<*, *> -> {
                    println(result.data)
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