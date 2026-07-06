package com.edurda77.download_install.downloader

import android.app.Application
import com.edurda77.download_install.downloader.remote.VersionResponse
import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.download_install.utils.DownloadDataError
import com.edurda77.download_install.utils.DownloadStatus
import com.edurda77.download_install.utils.ResultDownloadWork
import com.edurda77.download_install.utils.pathToDownloadFile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.io.File

internal class DownloadRepositoryImpl(
    private val httpClient: HttpClient,
    private val application: Application
) : DownloadRepository {

    override suspend fun getLastUpdateVersion(url: String): ResultDownloadWork<LastVersionApp, DownloadDataError> {
        return withContext(Dispatchers.IO) {
            try {
                val response = httpClient.get(url/*"${DOWNLOAD_URL}version"*/) {
                    contentType(ContentType.Application.Json)
                }.call
                if (response.response.status.isSuccess()) {
                    val content = response.response.body<VersionResponse>()
                    val version =
                        (content.groupResponseDto.name + "." + content.name).toDoubleOrNull()
                    if (version == null) {
                        ResultDownloadWork.Error(DownloadDataError.Network.UNKNOWN_VERSION)
                    } else {
                        val lastVersionApp = LastVersionApp(
                            name = content.groupResponseDto.appResponseDto.name,
                            lastVersion = version
                        )
                        ResultDownloadWork.Success(lastVersionApp)
                    }
                } else {
                    ResultDownloadWork.Error(DownloadDataError.Network.UNKNOWN)
                }
            } catch (e: ClientRequestException) {
                when (e.response.status.value) {
                    else -> ResultDownloadWork.Error(DownloadDataError.Network.BAD_REQUEST)
                }
            } catch (e: ServerResponseException) {
                e.printStackTrace()
                ResultDownloadWork.Error(DownloadDataError.Network.SERVER_ERROR)
            } catch (e: HttpRequestTimeoutException) {
                e.printStackTrace()
                ResultDownloadWork.Error(DownloadDataError.Network.REQUEST_TIMEOUT)
            } catch (e: Exception) {
                e.printStackTrace()
                ResultDownloadWork.Error(DownloadDataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun downloadFile(
        url: String,
        downloadedFileName: String,
    ): Flow<DownloadStatus> {
        return callbackFlow {
            val apkFilePath =
                application.pathToDownloadFile(downloadedFileName)
            try {
                send(DownloadStatus.Started)
                val response = httpClient.get(url/*"${DOWNLOAD_URL}file"*/) {
                    contentType(ContentType.Application.Json)
                    onDownload { bytesSentTotal, contentLength ->
                        contentLength?.let {
                            send(DownloadStatus.InProgress((bytesSentTotal*100/contentLength).toInt()))
                        }
                    }
                }
                if (response.status.isSuccess()) {
                    val file =
                        File(apkFilePath)
                    response.bodyAsChannel().copyAndClose(file.writeChannel())
                    send(DownloadStatus.Success)
                } else {
                    send(DownloadStatus.Error(DownloadDataError.Network.BAD_REQUEST))
                }
                close()
            } catch (e: Exception) {
                send(DownloadStatus.Error(DownloadDataError.Network.SERVER_ERROR))
            }
        }
    }
}