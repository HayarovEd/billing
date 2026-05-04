package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toBlocking
import com.impuls.billing.data.remote.BlockingDto
import com.impuls.billing.data.remote.SuccessRecordDto
import com.impuls.billing.data.remote.requests.BlockingRequest
import com.impuls.billing.domain.model.BlockVgroup
import com.impuls.billing.domain.repository.BlockingRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import com.impuls.billing.domain.utils.toBillingFormat
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

class BlockingRepositoryImpl(
    private val client: HttpClient,
): BlockingRepository {

    override suspend fun insertBlocking(
        token: String,
        vgId: Long,
        agrmId: Long,
        changeTime: LocalDate,
        blkReq: Int,
    ): ResultWork<Long, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.post ("block/rasp") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        setBody(
                            BlockingRequest(
                                agrmId = agrmId,
                                blkReq = blkReq,
                                changeTime = "${changeTime.year}-${changeTime.month.number}-${changeTime.day}",
                                vgId = vgId
                            )
                        )
                    }
                },
                data = {
                    it.body<SuccessRecordDto>().recordNumber
                }
            )
        }
    }

    override suspend fun getBlocking(
        token: String,
        vgId: Long,
        dateFrom: LocalDate,
        dateTo: LocalDate,
    ): ResultWork<List<BlockVgroup>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get ("block/rasp") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        parameter("vgid", vgId)
                        parameter("date_from", dateFrom.toBillingFormat())
                        parameter("date_to", dateTo.toBillingFormat())
                    }
                },
                data = {
                    it.body<List<BlockingDto>>().map { it.toBlocking() }
                }
            )
        }
    }
}