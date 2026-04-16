package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toClientVroup
import com.impuls.billing.data.remote.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.repository.HomeRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
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

class HomeRepositoryImpl(
    private val client: HttpClient,
) : HomeRepository {

    override suspend fun getShortStatistics(
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
                val a = it.bodyAsText()
                println(a)
                it.body<List<ClientVgroupFullResponse>>().map { it.toClientVroup() }
            }
        }
    }
}