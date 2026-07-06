package com.impuls.billing.ui.payment_history

import com.impuls.billing.ui.uikit.UiText

sealed interface HistoryPaymentScreenEvent {
    data class OnMessage(val message: UiText) :HistoryPaymentScreenEvent
    data object NavigateEvent : HistoryPaymentScreenEvent
}