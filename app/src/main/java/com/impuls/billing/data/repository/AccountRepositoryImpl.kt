package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toClientAccount
import com.impuls.billing.data.remote.client_account.ClientAccountResponse
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.repository.AccountRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
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
                client.get ("account/") {
                    bearerAuth(token)
                }
            ) {
               it.body<ClientAccountResponse>().toClientAccount()
            }
        }
    }
}