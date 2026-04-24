package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toClientAccount
import com.impuls.billing.data.mapper.toClientInfo
import com.impuls.billing.data.mapper.toClientNotices
import com.impuls.billing.data.mapper.toClientVroup
import com.impuls.billing.data.remote.ClientInfoDto
import com.impuls.billing.data.remote.SuccessDto
import com.impuls.billing.data.remote.SuccessRecordDto
import com.impuls.billing.data.remote.client_account.ClientAccountResponse
import com.impuls.billing.data.remote.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.data.remote.notices.ClientNoticesDto
import com.impuls.billing.data.remote.requests.CodeRequest
import com.impuls.billing.data.remote.requests.EmailRequest
import com.impuls.billing.data.remote.requests.MobilePhoneRequest
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.ClientInfo
import com.impuls.billing.domain.model.ClientNotices
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val client: HttpClient,
) : AccountRepository {
    override suspend fun getClientAccount(
        token: String
    ): ResultWork<ClientAccount, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.get("account/") {
                    bearerAuth(token)
                }
            ) {
                val a = it.bodyAsText()
                println(a)
                it.body<ClientAccountResponse>().toClientAccount()
            }
        }
    }

    override suspend fun setClientMobilePhone(
        token: String,
        uid: Long,
        phone: String
    ): ResultWork<Boolean, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.post("account/clientMobilePhone") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    setBody(
                        MobilePhoneRequest(
                            phone = phone,
                            uid = uid
                        )
                    )
                }
            ) {
                it.body<SuccessDto>().success
            }
        }
    }


    override suspend fun validateClientMobileNumber(token: String): ResultWork<Long, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.post("account/validationClientMobileNumber") {
                    bearerAuth(token)
                }
            ) {
                it.body<SuccessRecordDto>().recordNumber
            }
        }
    }

    override suspend fun confirmValidateClientMobileNumber(
        token: String,
        id: Long,
        code: String,
    ): ResultWork<Boolean, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.post("account/confirmationClientMobileNumber") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    setBody(
                        CodeRequest(
                            id = id,
                            code = code
                        )
                    )
                }
            ) {
                it.body<SuccessDto>().success
            }
        }
    }

    override suspend fun setClientEmail(
        token: String,
        uid: Long,
        email: String
    ): ResultWork<Boolean, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.post("account/clientEmail") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    setBody(
                        EmailRequest(
                            uid = uid,
                            email = email
                        )
                    )
                }
            ) {
                it.body<SuccessDto>().success
            }
        }
    }


    override suspend fun validateClientEmail(token: String): ResultWork<Long, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.post("account/validationClientEmail") {
                    bearerAuth(token)
                }
            ) {
                it.body<SuccessRecordDto>().recordNumber
            }
        }
    }

    override suspend fun confirmValidateClientEmail(
        token: String,
        id: Long,
        code: String,
    ): ResultWork<Boolean, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.post("account/confirmationClientEmail") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    setBody(
                        CodeRequest(
                            id = id,
                            code = code
                        )
                    )
                }
            ) {
                it.body<SuccessDto>().success
            }
        }
    }

    override suspend fun getVGroups(
        token: String,
        agrmId: Long?,
        vgId: Long?,
    ): ResultWork<List<ClientVroup>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.get("account/vgroup") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    agrmId?.let {
                        parameter("agrmid", it)
                    }
                    vgId?.let {
                        parameter("vgid", vgId)
                    }
                }
            ) {
                it.body<List<ClientVgroupFullResponse>>().map { it.toClientVroup() }
            }
        }
    }

    override suspend fun getClientInfo(
        token: String
    ): ResultWork<ClientInfo, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.get("account/info") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                }
            ) {
                it.body<ClientInfoDto>().toClientInfo()
            }
        }
    }

    override suspend fun getClientNotices(
        token: String,
        userid: Long,
    ): ResultWork<ClientNotices, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                client.get("account/notices") {
                    bearerAuth(token)
                    contentType(ContentType.Application.Json)
                    parameter("userid", userid)
                }
            ) {
                it.body<ClientNoticesDto>().toClientNotices()
            }
        }
    }
}