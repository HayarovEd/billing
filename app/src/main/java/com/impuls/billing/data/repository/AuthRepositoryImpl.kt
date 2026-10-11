package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.convertToRecoverPassword
import com.impuls.billing.data.remote.RecoverPasswordDto
import com.impuls.billing.data.remote.SuccessDto
import com.impuls.billing.data.remote.TokenDto
import com.impuls.billing.data.remote.requests.LoginRequest
import com.impuls.billing.data.remote.requests.RecoverPasswordRequest
import com.impuls.billing.data.remote.requests.UpdatePasswordByCodeRequest
import com.impuls.billing.domain.model.RecoverPassword
import com.impuls.billing.domain.repository.AuthRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.model.ResultLogin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val client: HttpClient,
) : AuthRepository {



    override suspend fun login(
        username: String,
        password: String
    ): ResultWork<ResultLogin, DataError> {
        return handleResponse(
            request = {
                client.post("auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        LoginRequest(
                            name = username,
                            password = password
                        )
                    )
                }

            },
            data = {
                val txt = it.bodyAsText()
                println(txt)
                ResultLogin(it.body<TokenDto>().token)
            }
        )
    }

    override suspend fun recoverPassword(
        login: String,
        transport: Int
    ): ResultWork<RecoverPassword, DataError> {
        return handleResponse(
            request = {
                client.post("auth/recoverPassword") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        RecoverPasswordRequest(
                            login = login,
                            transport = transport,
                            simpleRecover = true,
                            sync = true,
                            operId = 0
                        )
                    )
                }
            },
            data = {
                it.body<RecoverPasswordDto>().convertToRecoverPassword()
            }
        )
    }

    override suspend fun updatePasswordByCode(
        login: String,
        password: String,
        code: String,
    ): ResultWork<Boolean, DataError> {
        return handleResponse(
            request = {
                client.post("auth/updatePasswordByCode") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        UpdatePasswordByCodeRequest(
                            login = login,
                            password = password,
                            code = code
                        )
                    )
                }
            },
            data = {
                it.body<SuccessDto>().success
            }
        )
    }
}
