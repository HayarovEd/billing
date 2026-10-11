package com.edurda77.download_install.utils


sealed interface ResultDownloadWork<out D, out E: RootDownloaderError> {
    data class Success<out D, out E: RootDownloaderError>(val data: D): ResultDownloadWork<D, E>
    data class Error<out D, out E: RootDownloaderError>(val error: E): ResultDownloadWork<D, E>

}