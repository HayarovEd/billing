package com.edurda77.download_install.refresher

import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.download_install.utils.DownloadDataError
import com.edurda77.download_install.utils.DownloadStatus
import com.edurda77.download_install.utils.ResultDownloadWork
import kotlinx.coroutines.flow.Flow

interface Refresher {
    suspend fun getLastVersion(url: String): ResultDownloadWork<LastVersionApp, DownloadDataError>
    suspend fun updateInApp(url: String, downloadedFileName: String): Flow<DownloadStatus>
    suspend fun updateInBackground(url: String, downloadedFileName: String): Flow<DownloadStatus>
}