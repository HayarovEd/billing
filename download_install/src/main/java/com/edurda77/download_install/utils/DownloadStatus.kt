package com.edurda77.download_install.utils


sealed class DownloadStatus {
    data object Started : DownloadStatus()

    class InProgress(val percentage: Int) : DownloadStatus()

    data object Success : DownloadStatus()

    class Error(val error: DownloadDataError) : DownloadStatus()
}