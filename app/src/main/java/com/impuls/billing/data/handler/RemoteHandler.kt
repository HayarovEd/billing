package com.impuls.billing.data.handler

import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.ClosedByteChannelException
import java.net.ConnectException

suspend fun <D> handleResponse(
    request: suspend () -> HttpResponse,
    data: suspend (HttpResponse) -> D
): ResultWork<D, DataError> {
    return try {
        val response = request()
        if (response.status.isSuccess()) {
            ResultWork.Success(data(response))
        } else {
            when (response.status.value) {
                400 -> ResultWork.Error(DataError.Network.BAD_REQUEST)
                401 -> ResultWork.Error(DataError.Network.UNAUTHORIZED)
                else -> ResultWork.Error(DataError.Network.UNKNOWN)
            }
        }
    } catch (e: ClientRequestException) {
        when (e.response.status.value) {
            400 -> ResultWork.Error(DataError.Network.BAD_REQUEST)
            401 -> ResultWork.Error(DataError.Network.UNAUTHORIZED)
            else -> ResultWork.Error(DataError.Network.UNKNOWN)
        }
    } catch (e: ClosedByteChannelException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.SERVER_ERROR)
    } catch (e: ConnectException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.SERVER_ERROR)
    } catch (e: ServerResponseException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.SERVER_ERROR)
    } catch (e: HttpRequestTimeoutException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.NO_INTERNET)
    } catch (e: JsonConvertException) {
        e.printStackTrace()
        ResultWork.Error(DataError.SerializationError.FORMAT_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.UNKNOWN)
    }
}
