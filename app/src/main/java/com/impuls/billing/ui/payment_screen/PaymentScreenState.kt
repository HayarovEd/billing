package com.impuls.billing.ui.payment_screen

import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.PaymentToken
import com.impuls.billing.domain.model.payment.ResultCheck

data class PaymentScreenState(
    val number: String? = null,
    val companyPayment: CompanyPayment? = null,
    val isLoading: Boolean = false,
    val paymentToken: PaymentToken? = null,
    val imageData: ImageData? = null,
    val resultCheck: ResultCheck? = null,
    val amountString: String = "",
) {
    val amountNull = amountString.toIntOrNull()
    val typeErrorAmount = if (amountNull == null) {
        if (amountString.isBlank()) TypeErrorAmount.BLANK else TypeErrorAmount.NOT_DIGIT
    } else {
        if (amountNull < 50) TypeErrorAmount.LESS_RANGE
        else if (amountNull > 100000) TypeErrorAmount.BIG_RANGE
        else TypeErrorAmount.NO_ERROR
    }
}


enum class TypeErrorAmount {
    NO_ERROR,
    NOT_DIGIT,
    LESS_RANGE,
    BIG_RANGE,
    BLANK
}