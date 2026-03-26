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
    val resultCheck: ResultCheck? = null
)