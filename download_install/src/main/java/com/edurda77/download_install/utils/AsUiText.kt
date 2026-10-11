package com.edurda77.download_install.utils

import com.edurda77.download_install.R


fun DownloadDataError.asUiResultText(): UiResultText {
    return when (this) {
        DownloadDataError.Network.SERVER_ERROR -> UiResultText.StringResource(
            R.string.server_error
        )
        DownloadDataError.Network.BAD_REQUEST -> UiResultText.StringResource(
            R.string.bad_request
        )
        DownloadDataError.Network.REQUEST_TIMEOUT -> UiResultText.StringResource(
            R.string.timeout_error
        )
        DownloadDataError.Network.UNKNOWN_VERSION -> UiResultText.StringResource(
            R.string.unknown_version
        )
        DownloadDataError.Network.UNKNOWN -> UiResultText.StringResource(
            R.string.unknown
        )
    }
}