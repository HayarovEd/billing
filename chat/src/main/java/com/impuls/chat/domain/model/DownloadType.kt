package com.impuls.chat.domain.model

import android.net.Uri
import com.impuls.chat.domain.utils.ChatDataError


sealed interface DownloadType {
    data object Started : DownloadType

    data class Progress(
        val value: String
    ) : DownloadType

    data class Finished(
        val byteArray: ByteArray
    ) : DownloadType {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Finished
            return byteArray.contentEquals(other.byteArray)
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode()
        }
    }

    data class Failed(
        val error: ChatDataError
    ) : DownloadType
}


sealed interface DownloadTypeFromUseCase {
    data object Started : DownloadTypeFromUseCase

    data class Progress(
        val value: String
    ) : DownloadTypeFromUseCase

    data class Finished(
        val uri: Uri
    ) : DownloadTypeFromUseCase

    data class Failed(
        val error: ChatDataError
    ) : DownloadTypeFromUseCase
}
