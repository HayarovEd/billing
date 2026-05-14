package com.edurda77.download_install.utils

sealed interface DownloadDataError : RootDownloaderError {
    enum class Network: DownloadDataError {
        SERVER_ERROR,
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNKNOWN_VERSION,
        UNKNOWN
    }
}