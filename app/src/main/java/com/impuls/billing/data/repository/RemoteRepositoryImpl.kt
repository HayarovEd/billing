package com.impuls.billing.data.repository

import androidx.compose.runtime.mutableStateOf
import com.impuls.billing.data.mapper.SoapParser
import com.impuls.billing.data.mapper.toClientAccount
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.repository.RemoteRepository
import com.impuls.billing.domain.utils.BASE_URL
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.model.ResultLogin
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteRepositoryImpl(
    private val client: HttpClient,
) : RemoteRepository {

    private val cookie = mutableStateOf("")
    private val soapParser = SoapParser()


    override suspend fun login(
        username: String,
        password: String
    ): ResultWork<ResultLogin, DataError> {
        return handleResponse {
            val soapRequest = buildString {
                append("""<?xml version="1.0" encoding="UTF-8"?>""")
                append(
                    """
                <soapenv:Envelope 
                    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                    xmlns:lbapi="urn:api3">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <lbapi:ClientLogin>
                            <login>$username</login>
                            <pass>$password</pass>
                        </lbapi:ClientLogin>
                    </soapenv:Body>
                </soapenv:Envelope>
            """.trimIndent()
                )
            }
            val response = client.post(BASE_URL) {
                setBody(soapRequest)
            }
            response.headers["Set-Cookie"]?.let {
                cookie.value = it
            }

            val body = response.bodyAsText()
            parseLoginResponse(body)
        }
    }


    override suspend fun getClientAccount(): ResultWork<ClientAccount, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val soapRequest = buildString {
                    //append("""<?xml version="1.0" encoding="UTF-8"?>""")
                    append(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                                "xmlns:urn=\"urn:api3\">\n" +
                                "   <soapenv:Header/>\n" +
                                "   <soapenv:Body>\n" +
                                "      <urn:getClientAccount>\n" +
                                "         <!--Optional:-->\n" +
                                "         <flt>\n" +
                                "            <!--Optional:-->\n" +
                                "            <activonly>0</activonly>\n" +
                                "         </flt>\n" +
                                "      </urn:getClientAccount>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>".trimIndent()
                    )
                }
                val response = client.post(BASE_URL) {
                    setBody(soapRequest)
                    header("Cookie", cookie)
                }
                val xml = response.bodyAsText()
                soapParser.parseFullSoapResponse(xml).toClientAccount()
            }
        }
    }

    private fun parseLoginResponse(xml: String): ResultLogin {
        // Простой парсинг через regex (для продакшена лучше использовать XML парсер)
        val uidRegex = Regex("<uid>(\\d+)</uid>")
        //val timeRegex = Regex("<timelastlogin>(.*?)</timelastlogin>")
        val errorRegex = Regex("<faultstring>(.*?)</faultstring>")
        val uid = uidRegex.find(xml)?.groupValues?.get(1)?.toLongOrNull()
        val error = errorRegex.find(xml)?.groupValues?.get(1) ?: ""
        //val time = timeRegex.find(xml)?.groupValues?.get(1) ?: ""
        return if (uid == null) ResultLogin.Error(error) else ResultLogin.Success(
            uid
        )
    }


}