package com.impuls.billing.ui.payment_screen


fun backSwitcher(methodType: PaymentScreenType,
                 onBack: () -> Unit
): PaymentScreenType {
    return when (methodType) {
        PaymentScreenType.ENTER_AMOUNT -> {
            onBack()
            PaymentScreenType.ENTER_AMOUNT
        }
        PaymentScreenType.CHOOSE_PAYMENT_METHOD -> {
            PaymentScreenType.ENTER_AMOUNT
        }
        PaymentScreenType.CARD -> {
            PaymentScreenType.CHOOSE_PAYMENT_METHOD
        }
        PaymentScreenType.ENTER_PHONE -> {
            PaymentScreenType.CHOOSE_PAYMENT_METHOD
        }
        PaymentScreenType.SBER_PAY -> {
            PaymentScreenType.CHOOSE_PAYMENT_METHOD
        }
        PaymentScreenType.SBP -> {
            PaymentScreenType.CHOOSE_PAYMENT_METHOD
        }
    }
}