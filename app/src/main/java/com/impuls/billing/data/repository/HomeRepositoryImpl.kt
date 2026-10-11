package com.impuls.billing.data.repository

import com.impuls.billing.data.handler.handleResponse
import com.impuls.billing.data.mapper.toClientVGroup
import com.impuls.billing.data.mapper.toVgroupUsbBox
import com.impuls.billing.data.remote.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.data.remote.connected_usb_box.VgroupUsbBoxDto
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.VgroupUsbBox
import com.impuls.billing.domain.repository.HomeRepository
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
                request = {
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
                },
                data = {
                    it.body<List<ClientVgroupFullResponse>>().map { it.toClientVGroup() }
                }
            )
        }
    }

    override suspend fun getUsbBoxByVgroup(
        token: String,
        vgId: Long,
    ): ResultWork<List<VgroupUsbBox>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse(
                request = {
                    client.get("home/service") {
                        bearerAuth(token)
                        contentType(ContentType.Application.Json)
                        parameter("vgId", vgId)
                    }
                },
                data = {
                    it.body<List<VgroupUsbBoxDto>>().map { it.toVgroupUsbBox() }
                }
            )
        }
    }

}
