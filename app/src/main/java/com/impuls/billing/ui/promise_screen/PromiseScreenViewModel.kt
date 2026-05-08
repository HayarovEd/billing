package com.impuls.billing.ui.promise_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel

class PromiseScreenViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(PromiseScreenState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PromiseScreenState()
        )

    private val eventChannel = Channel<PromiseScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PromiseScreenAction) {
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

    private fun PromiseScreenState.updateState() {
        _state.update {
            this
        }
    }
}