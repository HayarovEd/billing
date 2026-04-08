package com.impuls.billing.data.repository

import com.impuls.billing.data.remote.TokenDto
import com.impuls.billing.data.remote.requests.LoginRequest
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.repository.RemoteRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.model.ResultLogin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RemoteRepositoryImpl(
    private val client: HttpClient,
) : RemoteRepository {



    override suspend fun login(
        username: String,
        password: String
    ): ResultWork<ResultLogin, DataError> {
        return handleResponse(
            response = client.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginRequest(
                        name = username,
                        password = password
                    )
                )
            },
            data = {
                ResultLogin(it.body<TokenDto>().token)
            }
        )
    }


    override suspend fun getClientAccount(): ResultWork<ClientAccount, DataError> {
        return TODO()/* withContext(Dispatchers.IO) {
            handleResponse {
                val soapRequest = buildString {
                    append(
                        setBaseAppend(
                            "<urn:getClientAccount>\n</urn:getClientAccount>\n"
                        )
                    )
                }
                val response = client.post(BASE_URL) {
                    setBody(soapRequest)
                    header("Cookie", cookie)
                }
                val xml = response.bodyAsText()
                soapParser.parseFullSoapResponse(xml).toClientAccount()
            }
        }*/
    }

    override suspend fun setClientMobilePhone(
        uid: Int,
        phone: String
    ): ResultWork<Boolean, DataError> {
        return TODO()/*withContext(Dispatchers.IO) {
            handleResponse {
                val soapRequest = buildString {
                    append(
                        setBaseAppend(
                            " <urn:setClientInfo>\n" +
                                    "         <val>\n" +
                                    "            <userid>${uid}</userid>\n" +
                                    "            <mobile>${phone}</mobile>\n" +
                                    "         </val>\n" +
                                    "      </urn:setClientInfo>"
                        )
                    )
                }
                val response = client.post(BASE_URL) {
                    setBody(soapRequest)
                    header("Cookie", cookie)
                }
                val xml = response.bodyAsText()
                soapParser.parseSetClientInfoResponse(xml) == 1
            }
        }*/
    }


    override suspend fun validateClientMobileNumber(): ResultWork<Int, DataError> {
        return TODO()/*handleResponse {
            val soapRequest = buildString {
                append(
                    setBaseAppend(
                        "<urn:validateClientMobileNumber/>\n"
                    )
                )
            }
            val response = client.post(BASE_URL) {
                setBody(soapRequest)
                header("Cookie", cookie)
            }
            val xml = response.bodyAsText()
            soapParser.parseSetClientInfoResponse(xml)
        }*/
    }

    override suspend fun confirmValidateClientMobileNumber(
        id: Int,
        code: String,
    ): ResultWork<Boolean, DataError> {
        return TODO()/* handleResponse {
            val soapRequest = buildString {
                append(
                    setBaseAppend(
                       " <urn:confirmValidateClientMobileNumber>\n" +
                               "         <id>$id/id>\n" +
                               "         <code>$code</code>\n" +
                               "      </urn:confirmValidateClientMobileNumber>\n"
                    )
                )
            }
            val response = client.post(BASE_URL) {
                setBody(soapRequest)
                header("Cookie", cookie)
            }
            val xml = response.bodyAsText()
            soapParser.parseSetClientInfoResponse(xml) == 1
        }*/
    }

    override suspend fun setClientEmail(
        uid: Int,
        email: String
    ): ResultWork<Boolean, DataError> {
        return TODO()/*withContext(Dispatchers.IO) {
            handleResponse {
                val soapRequest = buildString {
                    append(
                        setBaseAppend(
                            " <urn:setClientInfo>\n" +
                                    "         <val>\n" +
                                    "            <userid>${uid}</userid>\n" +
                                    "            <email>${email}</email>\n" +
                                    "         </val>\n" +
                                    "      </urn:setClientInfo>"
                        )
                    )
                }
                val response = client.post(BASE_URL) {
                    setBody(soapRequest)
                    header("Cookie", cookie)
                }
                val xml = response.bodyAsText()
                soapParser.parseSetClientInfoResponse(xml) == 1
            }*/
    }


    override suspend fun validateClientEmail(): ResultWork<Int, DataError> {
        return TODO()/*handleResponse {
            val soapRequest = buildString {
                append(
                    setBaseAppend(
                        "  <urn:validateClientEmail/>\n"
                    )
                )
            }
            val response = client.post(BASE_URL) {
                setBody(soapRequest)
                header("Cookie", cookie)
            }
            val xml = response.bodyAsText()
            soapParser.parseSetClientInfoResponse(xml)
        }*/
    }

    override suspend fun confirmValidateClientEmail(
        id: Int,
        code: String,
    ): ResultWork<Boolean, DataError> {
        return TODO()/*handleResponse {
            val soapRequest = buildString {
                append(
                    setBaseAppend(
                        " <urn:confirmValidateClientEmail>\n" +
                                "         <id>$id/id>\n" +
                                "         <code>$code</code>\n" +
                                "      </urn:confirmValidateClientEmail>\n"
                    )
                )
            }
            val response = client.post(BASE_URL) {
                setBody(soapRequest)
                header("Cookie", cookie)
            }
            val xml = response.bodyAsText()
            soapParser.parseSetClientInfoResponse(xml) == 1
        }*/
    }



    private fun setBaseAppend(innerContent: String): String {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:urn=\"urn:api3\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                innerContent +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>".trimIndent()
    }

}