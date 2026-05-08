package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toHistoryDebit
import com.impuls.billing.data.mapper.toHistoryPayment
import com.impuls.billing.data.mapper.toPPSettings
import com.impuls.billing.data.remote.DebitDto
import com.impuls.billing.data.remote.PPSettingsDto
import com.impuls.billing.data.remote.RecommendedPaymentDto
import com.impuls.billing.data.remote.payment_history.PaymentHistoryDto
import com.impuls.billing.domain.model.HistoryDebit
import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.model.PPSettings
import com.impuls.billing.domain.repository.BillingPaymentRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.utils.toBillingFormat
import com.impuls.billing.domain.utils.toBillingFormat2
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class BillingPaymentRepositoryImpl(
    private val client: HttpClient,
): BillingPaymentRepository {

    override suspend fun getPPSettings(
        token: String,
        agrmId: Long,
    ): ResultWork<PPSettings, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("payment/ppSettings") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        parameter("agrm_id", agrmId)
                    }
                },
                data = {
                    it.body<PPSettingsDto>().toPPSettings()
                }
            )
        }
    }

    override suspend fun getRecommendedPayment(
        token: String,
        agrmId: Long,
        mode: Long,
    ): ResultWork<Double, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("payment/recommended") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        parameter("agrm_id", agrmId)
                        parameter("mode", mode)
                    }
                },
                data = {
                    it.body<RecommendedPaymentDto>().recommendedPayment
                }
            )
        }
    }

    override suspend fun getHistoryOfPayment(
        token: String,
        agrmId: Long,
        dateFrom: LocalDate,
        dateTo: LocalDate,
    ): ResultWork<List<HistoryPayment>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("payment/payment") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        parameter("agrmnum", agrmId)
                        parameter("dtto", dateTo.toBillingFormat())
                        parameter("dtfrom", dateFrom.toBillingFormat())
                    }
                },
                data = {
                    it.body<List<PaymentHistoryDto>>().map { it.toHistoryPayment() }
                }
            )
        }
    }

    override suspend fun getHistoryOfDebet(
        token: String,
        agrmId: Long,
        vgId: Long,
        tarifId: Long,
        dateFrom: LocalDate,
        dateTo: LocalDate,
    ): ResultWork<List<HistoryDebit>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("payment/debits") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        parameter("armgid", agrmId)
                        parameter("tarifid", tarifId)
                        parameter("vgid", vgId)
                        parameter("dtto", dateTo
                            .toBillingFormat2())
                        parameter("dtfrom", dateFrom
                            .toBillingFormat2())
                    }
                },
                data = {
                    it.body<List<DebitDto>>().map { it.toHistoryDebit() }
                }
            )
        }
    }
}
