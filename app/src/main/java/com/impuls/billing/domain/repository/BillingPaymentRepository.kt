package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.HistoryDebit
import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.model.PPSettings
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

    suspend fun getHistoryOfDebet(
        token: String,
        agrmId: Long,
        vgId: Long,
        tarifId: Long,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): ResultWork<List<HistoryDebit>, DataError>

    suspend fun getPPSettings(token: String, agrmId: Long): ResultWork<PPSettings, DataError>
    suspend fun getRecommendedPayment(
        token: String,
        agrmId: Long,
        mode: Long = 0
    ): ResultWork<Double, DataError>

    suspend fun setPromisePayment(
        token: String,
        agrmId: Long,
        amount: Double
    ): ResultWork<Boolean, DataError>
}