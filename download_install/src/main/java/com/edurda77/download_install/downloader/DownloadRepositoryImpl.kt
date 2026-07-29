package com.edurda77.download_install.downloader

import android.app.Application
import android.os.StatFs
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
            val apkFilePath = application.pathToDownloadFile(downloadedFileName)
            val file = File(apkFilePath)
            
            try {
                cleanupOldApks(downloadedFileName)
                send(DownloadStatus.Started)
                
                var lastEmittedProgress = -1
                
                val response = httpClient.get(url) {
                    contentType(ContentType.Application.Json)
                    onDownload { bytesSentTotal, contentLength ->
                        contentLength?.let {
                            if (!checkAvailableSpace(contentLength)) {
                                trySend(DownloadStatus.Error(DownloadDataError.Network.SERVER_ERROR))
                                // Note: We can't easily cancel the Ktor call here without more architecture
                                return@onDownload
                            }
                            
                            val progress = (bytesSentTotal * 100 / contentLength).toInt()
                            if (progress > lastEmittedProgress) {
                                lastEmittedProgress = progress
                                trySend(DownloadStatus.InProgress(progress))
                            }
                        }
                    }
                }
                
                if (response.status.isSuccess()) {
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

    private fun checkAvailableSpace(requiredBytes: Long): Boolean {
        val path = application.externalCacheDir?.absolutePath ?: application.cacheDir.absolutePath
        return try {
            val stat = StatFs(path)
            val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
            availableBytes > requiredBytes + (20 * 1024 * 1024) // 20MB safety buffer
        } catch (e: Exception) {
            true // Fallback to true if we can't check
        }
    }

    private fun cleanupOldApks(currentFileName: String) {
        try {
            val dir = application.externalCacheDir ?: application.cacheDir
            dir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".apk") && file.name != currentFileName) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }
}