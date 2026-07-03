package com.impuls.chat.ui.main_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface MainScreenEvent {
    data class OnMessage(val message: UiText) :MainScreenEvent
    data class NavigateEvent(val chatId:String) : MainScreenEvent
    data class MessageEvent(val messageEvent: MessageErrorEvent) : MainScreenEvent
}


enum class MessageErrorEvent{
    MESSAGE_EMPTY,
    WRONG_EMAIL,
    PHONE_EMPTY,
    NAME_EMPTY,
}