package com.impuls.billing.ui.payment_screen

sealed interface PaymentScreenAction {
    class UpdateAmount(val amount: String): PaymentScreenAction
}