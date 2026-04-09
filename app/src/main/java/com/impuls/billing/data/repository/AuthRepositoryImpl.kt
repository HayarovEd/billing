package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.remote.TokenDto
import com.impuls.billing.data.remote.requests.LoginRequest
import com.impuls.billing.domain.repository.AuthRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.ui.uikit.model.ResultLogin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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
}