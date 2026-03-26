package com.impuls.billing.ui.payment_screen

sealed interface PaymentScreenAction {
    class UpdateAmount(val amount: String): PaymentScreenAction
    class UpdatePaymentScreenType(val paymentScreenType: PaymentScreenType): PaymentScreenAction
    class UpdateMethodPayment(val typePayment: TypePayment): PaymentScreenAction
}