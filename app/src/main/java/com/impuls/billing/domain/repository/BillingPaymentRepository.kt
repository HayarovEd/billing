package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import kotlinx.datetime.LocalDateTime

interface BillingPaymentRepository {
    suspend fun getHistoryOfPayment(
        token: String,
        agrmId: Long,
        dateFrom: LocalDateTime,
        dateTo: LocalDateTime
    ): ResultWork<List<HistoryPayment>, DataError>
}