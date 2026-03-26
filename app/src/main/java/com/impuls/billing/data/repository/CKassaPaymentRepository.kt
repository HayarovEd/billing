package com.impuls.billing.data.repository

import com.impuls.billing.data.mapper.convertCompanyPayment
import com.impuls.billing.data.mapper.convertToImageData
import com.impuls.billing.data.mapper.convertToPaymentToken
import com.impuls.billing.data.mapper.convertToResultCheck
import com.impuls.billing.data.mapper.toPaymentCard
import com.impuls.billing.data.remote.check_account.SuccessCheckLsResponse
import com.impuls.billing.data.remote.image_data.ImageDataResponse
import com.impuls.billing.data.remote.payment.base_data.CompanyPaymentResponse
import com.impuls.billing.data.remote.payment_card.PayCardRequest
import com.impuls.billing.data.remote.payment_card.PaymentCardResponse
import com.impuls.billing.data.remote.payment_card.PaymentToolDto
import com.impuls.billing.data.remote.payment_card.PropertiesCardDto
import com.impuls.billing.data.remote.payment_token.PaymentTokenReponse
import com.impuls.billing.domain.model.payment.CompanyPayment
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.model.payment.PaymentCard
import com.impuls.billing.domain.model.payment.PaymentToken
import com.impuls.billing.domain.model.payment.ResultCheck
import com.impuls.billing.domain.repository.PaymentRepository
import com.impuls.billing.domain.utils.BODY_CKASSA_INIT_SERVICE
import com.impuls.billing.domain.utils.CONTENT_TYPE
import com.impuls.billing.domain.utils.CONTENT_TYPE_X_GWT
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.NUMBER_NPO_CODIT
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.utils.X_GWT_MODULE_BASE
import com.impuls.billing.domain.utils.X_GWT_MODULE_BASE_VALUE
import com.impuls.billing.domain.utils.X_GWT_PERMUTATION
import com.impuls.billing.domain.utils.X_GWT_PERMUTATION_VALUE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CKassaPaymentRepository(
    private val client: HttpClient,
) : PaymentRepository {

    /*override*/ suspend fun loadPaymentService(

    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = client.post("https://ckassa.ru/payment/payment/PaymentService") {
                    contentType(ContentType.Application.Json)
                    header(X_GWT_PERMUTATION, X_GWT_PERMUTATION_VALUE)
                    header(X_GWT_MODULE_BASE, X_GWT_MODULE_BASE_VALUE)
                    header(CONTENT_TYPE, CONTENT_TYPE_X_GWT)
                    setBody(
                        BODY_CKASSA_INIT_SERVICE
                    )
                }
                val stringResult = result.bodyAsText()
            }
        }
    }

    override suspend fun loadPaymentData(): ResultWork<CompanyPayment, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result =
                    client.post("https://pfapi.ckassa.ru/api-shop/rs/payform/provider-info/$NUMBER_NPO_CODIT") {
                        contentType(ContentType.Application.Json)
                    }
                result.call.body<CompanyPaymentResponse>().convertCompanyPayment()
            }
        }
    }

    override suspend fun loadToken(): ResultWork<PaymentToken, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result =
                    client.post("https://pfapi.ckassa.ru/api-shop/rs/payform/csrf/create/$NUMBER_NPO_CODIT") {
                        contentType(ContentType.Application.Json)
                    }
                result.call.body<PaymentTokenReponse>().convertToPaymentToken()
            }
        }
    }

    override suspend fun loadLogoUrl(
        category: String,
        service: String,
    ): ResultWork<ImageData, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result =
                    client.get("https://cabinet.ckassa.ru/payframe/api/personalization?category=$category&service=$service") {
                        contentType(ContentType.Application.Json)
                    }
                result.call.body<ImageDataResponse>().convertToImageData()
            }
        }
    }

    override suspend fun checkAccount(
        number: String,
    ): ResultWork<ResultCheck, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result =
                    client.post("https://pfapi.ckassa.ru/api-shop/rs/payform/check-pers-acc/$NUMBER_NPO_CODIT") {
                        contentType(ContentType.Application.Json)
                        setBody(
                            "{\"properties\":{\"Л_СЧЕТ\":\"$number\"}}"
                        )
                    }
                result.call.body<SuccessCheckLsResponse>().convertToResultCheck()
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
            handleResponse {
                val result =
                    client.post("https://pfapi.ckassa.ru/api-shop/rs/payform/do-pay/$NUMBER_NPO_CODIT") {
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
                result.call.body<PaymentCardResponse>().toPaymentCard()
            }
        }
    }

}