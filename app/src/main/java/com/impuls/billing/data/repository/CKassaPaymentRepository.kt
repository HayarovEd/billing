package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.convertCompanyPayment
import com.impuls.billing.data.mapper.convertToImageData
import com.impuls.billing.data.mapper.convertToResultCheck
import com.impuls.billing.data.mapper.toCheckPaymentStatus
import com.impuls.billing.data.mapper.toPaymentCard
import com.impuls.billing.data.mapper.toPaymentSber
import com.impuls.billing.data.mapper.toPaymentSbp
import com.impuls.billing.data.remote.check_account.SuccessCheckLsResponse
import com.impuls.billing.data.remote.check_status_payment.CheckPaymentStatusResponse
import com.impuls.billing.data.remote.image_data.ImageDataResponse
import com.impuls.billing.data.remote.payment.base_data.CompanyPaymentResponse
import com.impuls.billing.data.remote.payment_card.PayCardRequest
import com.impuls.billing.data.remote.payment_card.PaymentCardResponse
import com.impuls.billing.data.remote.payment_card.PaymentToolDto
import com.impuls.billing.data.remote.payment_card.PropertiesCardDto
import com.impuls.billing.data.remote.payment_sberpay.PaymentCberToolDto
import com.impuls.billing.data.remote.payment_sberpay.PaymentSberRequest
import com.impuls.billing.data.remote.payment_sberpay.PaymentSberResponse
import com.impuls.billing.data.remote.payment_sberpay.PropertiesSberDto
import com.impuls.billing.data.remote.payment_sbp.PaymentSbpResponse
import com.impuls.billing.domain.model.payment.CheckPaymentStatus
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.PaymentCard
import com.impuls.billing.domain.model.payment.PaymentSber
import com.impuls.billing.domain.model.payment.PaymentSbp
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.domain.repository.PaymentRepository
import com.impuls.billing.domain.utils.BASE_PAYMENT_URL
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.NUMBER_NPO_CODIT
import com.impuls.billing.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CKassaPaymentRepository(
    private val client: HttpClient,
) : PaymentRepository {


    override suspend fun loadPaymentData(): ResultWork<CompanyPayment, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("${BASE_PAYMENT_URL}provider-info/$NUMBER_NPO_CODIT") {
                        contentType(ContentType.Application.Json)
                    }
                },
            ) {
                it.call.body<CompanyPaymentResponse>().convertCompanyPayment()
            }
        }
    }

    override suspend fun loadLogoUrl(
        category: String,
        service: String,
    ): ResultWork<ImageData, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(request = {
                client.get("https://cabinet.ckassa.ru/payframe/api/personalization?category=$category&service=$service") {
                    contentType(ContentType.Application.Json)
                }
            }) {
                it.call.body<ImageDataResponse>().convertToImageData()
            }
        }
    }

    override suspend fun checkAccount(
        number: String,
    ): ResultWork<ResultCheck, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(request = {
                client.post("${BASE_PAYMENT_URL}check-pers-acc/$NUMBER_NPO_CODIT") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        "{\"properties\":{\"Л_СЧЕТ\":\"$number\"}}"
                    )
                }
            }) {
                it.call.body<SuccessCheckLsResponse>().convertToResultCheck()
            }
        }
    }

    override suspend fun payCard(
        amount: Int,
        comission: Int,
        greenZone: Boolean,
        offline: Boolean,
        payCurrency: String,
        type: String,
        ledgerAccount: String
    ): ResultWork<PaymentCard, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(request = {
                client.post("${BASE_PAYMENT_URL}do-pay/$NUMBER_NPO_CODIT") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        PayCardRequest(
                            amount = amount * 100,
                            comission = comission,
                            greenZone = greenZone,
                            offline = offline,
                            payCurrency = payCurrency,
                            paymentTool = PaymentToolDto(type = type),
                            properties = PropertiesCardDto(ledgerAccount = ledgerAccount)
                        )
                    )
                }
            }) {
                it.call.body<PaymentCardResponse>().toPaymentCard()
            }
        }
    }

    override suspend fun paySber(
        amount: Int,
        comission: Int,
        greenZone: Boolean,
        offline: Boolean,
        payCurrency: String,
        type: String,
        ledgerAccount: String,
        phone: String,
    ): ResultWork<PaymentSber, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(request = {
                client.post("${BASE_PAYMENT_URL}do-pay/$NUMBER_NPO_CODIT") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        PaymentSberRequest(
                            amount = amount * 100,
                            comission = comission,
                            greenZone = greenZone,
                            offline = offline,
                            payCurrency = payCurrency,
                            paymentTool = PaymentCberToolDto(
                                phone = phone,
                                type = type
                            ),
                            properties = PropertiesSberDto(ledgerAccount = ledgerAccount)
                        )
                    )
                }
            }) {
                it.call.body<PaymentSberResponse>().toPaymentSber()
            }
        }
    }


    override suspend fun paySbp(
        amount: Int,
        comission: Int,
        greenZone: Boolean,
        offline: Boolean,
        payCurrency: String,
        type: String,
        ledgerAccount: String,
    ): ResultWork<PaymentSbp, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("${BASE_PAYMENT_URL}do-pay/$NUMBER_NPO_CODIT") {
                        contentType(ContentType.Application.Json)
                        setBody(
                            PayCardRequest(
                                amount = amount * 100,
                                comission = comission,
                                greenZone = greenZone,
                                offline = offline,
                                payCurrency = payCurrency,
                                paymentTool = PaymentToolDto(type = type),
                                properties = PropertiesCardDto(ledgerAccount = ledgerAccount)
                            )
                        )
                    }
                }
            ) {
                it.call.body<PaymentSbpResponse>().toPaymentSbp()
            }
        }
    }




    override suspend fun checkStatusPayment(
        regPayNum: String
    ): ResultWork<CheckPaymentStatus, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post("${BASE_PAYMENT_URL}payment-info/$regPayNum") {
                        contentType(ContentType.Application.Json)
                    }
                }
            ) {
                it.call.body<CheckPaymentStatusResponse>().toCheckPaymentStatus()
            }
        }
    }

}