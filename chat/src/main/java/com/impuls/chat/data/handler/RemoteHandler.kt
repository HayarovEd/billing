package com.impuls.chat.data.handler


import android.util.Log
import com.impuls.chat.data.remote.ErrorDto
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.ClosedByteChannelException
import java.net.ConnectException

internal suspend fun <D> handleResponse(
    request: suspend () -> HttpResponse,
    data: suspend (HttpResponse) -> D
): ResultWork<D, ChatDataError> {
    return try {
        val response = request()
        if (response.status.isSuccess()) {
            ResultWork.Success(data(response))
        } else {
            Log.e("TAG", "Server returned error: ${response.status.value}")
            when (response.status.value) {
                400 -> ResultWork.Error(ChatDataError.CustomErrorChat(response.body<ErrorDto>().message))
                401 -> ResultWork.Error(ChatDataError.Network.UNAUTHORIZED)
                else -> ResultWork.Error(ChatDataError.Network.UNKNOWN)
            }
        }
    } catch (e: ClientRequestException) {
        Log.e("TAG", "ClientRequestException: ${e.response.status.value}", e)
        when (e.response.status.value) {
            400 -> ResultWork.Error(ChatDataError.Network.BAD_REQUEST)
            401 -> ResultWork.Error(ChatDataError.Network.UNAUTHORIZED)
            else -> ResultWork.Error(ChatDataError.Network.UNKNOWN)
        }
    } catch (e: ClosedByteChannelException) {
        Log.e("TAG", "ClosedByteChannelException", e)
        ResultWork.Error(ChatDataError.Network.SERVER_ERROR)
    } catch (e: ConnectException) {
        Log.e("TAG", "ConnectException", e)
        ResultWork.Error(ChatDataError.Network.SERVER_ERROR)
    } catch (e: ServerResponseException) {
        Log.e("TAG", "ServerResponseException", e)
        ResultWork.Error(ChatDataError.Network.SERVER_ERROR)
    } catch (e: HttpRequestTimeoutException) {
        Log.e("TAG", "HttpRequestTimeoutException", e)
        ResultWork.Error(ChatDataError.Network.REQUEST_TIMEOUT)
    } catch (e: UnresolvedAddressException) {
        Log.e("TAG", "UnresolvedAddressException", e)
        ResultWork.Error(ChatDataError.Network.NO_INTERNET)
    }  catch (e: Exception) {
        Log.e("TAG", "General Exception in handleResponse", e)
        ResultWork.Error(ChatDataError.Network.UNKNOWN)
    }
}
