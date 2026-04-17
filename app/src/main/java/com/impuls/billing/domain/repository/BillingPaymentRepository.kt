package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import kotlinx.datetime.LocalDate

interface BillingPaymentRepository {
    suspend fun getHistoryOfPayment(
        token: String,
        agrmId: Long,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): ResultWork<List<HistoryPayment>, DataError>
}