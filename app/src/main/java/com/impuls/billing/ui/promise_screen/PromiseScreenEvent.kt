package com.impuls.billing.ui.promise_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface PromiseScreenEvent {
    data class OnMessage(val message: UiText) :PromiseScreenEvent
    data object NavigateEvent : PromiseScreenEvent
}