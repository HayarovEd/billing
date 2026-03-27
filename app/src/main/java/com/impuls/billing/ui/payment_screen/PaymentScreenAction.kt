package com.impuls.billing.ui.payment_screen

sealed interface PaymentScreenAction {
    class UpdateAmount(val amount: String): PaymentScreenAction
    class UpdatePaymentScreenType(val paymentScreenType: PaymentScreenType): PaymentScreenAction
    class UpdateMethodPayment(val typePayment: TypePayment): PaymentScreenAction
    data object Payment: PaymentScreenAction
    class UpdatePhone(val phone: String): PaymentScreenAction
    data object SendSpbLink: PaymentScreenAction
    data object CheckPayment: PaymentScreenAction
}