package com.impuls.billing.ui.payment_screen

import com.impuls.billing.R
import com.impuls.billing.domain.model.payment.CheckPaymentStatus
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.PaymentCard
import com.impuls.billing.domain.model.payment.PaymentSber
import com.impuls.billing.domain.model.payment.PaymentSbp
import com.impuls.billing.domain.model.payment.PaymentToken
import com.impuls.billing.domain.model.payment.ResultCheck

data class PaymentScreenState(
    val paymentScreenType: PaymentScreenType = PaymentScreenType.ENTER_AMOUNT,
    val number: String? = null,
    val companyPayment: CompanyPayment? = null,
    val isLoading: Boolean = false,
    val paymentToken: PaymentToken? = null,
    val imageData: ImageData? = null,
    val resultCheck: ResultCheck? = null,
    val amountString: String = "",
    val chosenMethod: TypePayment? = null,
    val paymentCard: PaymentCard? = null,
    val paymentPhone: String = "",
    val paymentSber: PaymentSber? = null,
    val checkPaymentStatus: CheckPaymentStatus? = null,
    val paymentSbp: PaymentSbp? = null,
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

enum class PaymentScreenType {
    ENTER_AMOUNT,
    CHOOSE_PAYMENT_METHOD,
    CARD,
    ENTER_PHONE,
    SBER_PAY,
    SBP
}

enum class TypePayment(val comission: Int, val icons: List<Int>) {
    CARD(
        comission = 0,
        icons = listOf(
            R.drawable.visaa,
            R.drawable.master,
            R.drawable.mir_logo,
            R.drawable.pay_union_pay
        )
    ),
    SBER_PAY(
        comission = 0,
        icons = listOf(R.drawable.sber_pay)
    ),
    SBP(
        comission = 0,
        icons = listOf(R.drawable.sbp)
    ),
}
