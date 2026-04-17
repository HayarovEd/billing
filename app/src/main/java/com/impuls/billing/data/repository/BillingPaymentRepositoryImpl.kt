package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toHistoryPayment
import com.impuls.billing.data.remote.payment_history.PaymentHistoryDto
import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.repository.BillingPaymentRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.utils.toBillingFormat
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class BillingPaymentRepositoryImpl(
    private val client: HttpClient,
): BillingPaymentRepository {

    override suspend fun getHistoryOfPayment(
        token: String,
        agrmId: Long,
        dateFrom: LocalDateTime,
        dateTo: LocalDateTime,
    ): ResultWork<List<HistoryPayment>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.get("payment/paymen") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    parameter("agrmnum", agrmId)
                    parameter("dtto", dateTo.toBillingFormat())
                    parameter("dtfrom", dateFrom.toBillingFormat())
                }
            ) {
                val a = it.bodyAsText()
                println(a)
                it.body<List<PaymentHistoryDto>>().map { it.toHistoryPayment() }
            }
        }
    }
}