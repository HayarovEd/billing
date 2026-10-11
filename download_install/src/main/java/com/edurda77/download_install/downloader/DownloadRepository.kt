package com.edurda77.download_install.downloader

import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.download_install.utils.DownloadDataError
import com.edurda77.download_install.utils.DownloadStatus
import com.edurda77.download_install.utils.ResultDownloadWork
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun getLastUpdateVersion(
        url: String
    ): ResultDownloadWork<LastVersionApp, DownloadDataError>

    suspend fun downloadFile(
        url: String,
        downloadedFileName: String
    ): Flow<DownloadStatus>
}