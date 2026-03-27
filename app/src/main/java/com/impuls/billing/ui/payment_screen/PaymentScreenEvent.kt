package com.impuls.billing.ui.payment_screen

import com.impuls.billing.ui.uikit.UiText

sealed interface PaymentScreenEvent {
    data class OnMessage(val message: UiText): PaymentScreenEvent
    data class SendLink(val url: String): PaymentScreenEvent
}