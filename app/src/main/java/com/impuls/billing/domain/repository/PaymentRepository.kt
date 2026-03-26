package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.PaymentToken
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork

interface PaymentRepository {
    suspend fun loadPaymentData(): ResultWork<CompanyPayment, DataError>
    suspend fun loadToken(): ResultWork<PaymentToken, DataError>
    suspend fun loadLogoUrl(category: String, service: String): ResultWork<ImageData, DataError>
    suspend fun checkAccount(number: String): ResultWork<ResultCheck, DataError>
}