package com.edurda77.download_install.refresher

import android.app.Application
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.edurda77.download_install.downloader.DownloadRepository
import com.edurda77.download_install.installer.Installer
import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.download_install.utils.DownloadDataError
import com.edurda77.download_install.utils.DownloadStatus
import com.edurda77.download_install.utils.FILE_NAME
import com.edurda77.download_install.utils.FILE_URL
import com.edurda77.download_install.utils.KEY_PROGRESS
import com.edurda77.download_install.utils.ResultDownloadWork
import com.edurda77.download_install.worker.DownloadUpdateWorker
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class RefresherImpl(
    private val application: Application,
    private val downloadRepository: DownloadRepository,
    private val installer: Installer
) : Refresher {

    override suspend fun getLastVersion(url: String): ResultDownloadWork<LastVersionApp, DownloadDataError> =
        downloadRepository.getLastUpdateVersion(url)


    override suspend fun updateInApp(
        url: String,
        downloadedFileName: String,
    ): Flow<DownloadStatus> {
        return flow {
            downloadRepository.downloadFile(
                url = url,
                downloadedFileName = downloadedFileName
            ).collect { collector ->
                when (collector) {
                    is DownloadStatus.Error -> {
                        emit(DownloadStatus.Error(collector.error))
                    }

                    is DownloadStatus.InProgress -> {
                        emit(DownloadStatus.InProgress(collector.percentage))
                    }

                    DownloadStatus.Started -> {
                        emit(DownloadStatus.Started)
                    }

                    DownloadStatus.Success -> {
                        installer.installAPK(downloadedFileName)
                        emit(DownloadStatus.Success)
                    }
                }
            }
        }
    }

    override suspend fun updateInBackground(
        url: String,
        downloadedFileName: String,
    ): Flow<DownloadStatus> {
        return callbackFlow {
            val workerData = Data.Builder().apply {
                putString(FILE_URL, url)
                putString(FILE_NAME, downloadedFileName)
            }
            send(DownloadStatus.Started)
            val workerRequest = OneTimeWorkRequestBuilder<DownloadUpdateWorker>()
                /*.setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    duration = Duration.ofSeconds(15)
                )*/
                .setInputData(workerData.build())
                .build()
            val workManager = WorkManager.getInstance(application)
            workManager.enqueueUniqueWork(
                "UpdateDownloadWork",
                ExistingWorkPolicy.REPLACE,
                workerRequest
            )
            
            launch {
                workManager.getWorkInfoByIdFlow(workerRequest.id).collect { it ->
                    when (it?.state) {
                        WorkInfo.State.RUNNING -> {
                            val progress = it.progress.getInt(KEY_PROGRESS, 0)
                            trySend(DownloadStatus.InProgress(progress))
                        }

                        WorkInfo.State.SUCCEEDED -> {
                            installer.installAPK(downloadedFileName)
                            trySend(DownloadStatus.Success)
                            close()
                        }

                        WorkInfo.State.FAILED -> {
                            trySend(DownloadStatus.Error(DownloadDataError.Network.SERVER_ERROR))
                            close()
                        }

                        WorkInfo.State.CANCELLED -> {
                            trySend(DownloadStatus.Error(DownloadDataError.Network.UNKNOWN))
                            close()
                        }

                        else -> Unit
                    }
                }
            }
            awaitClose {
                workManager.cancelWorkById(workerRequest.id)
            }
        }
    }
}