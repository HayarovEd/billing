package com.impuls.chat.domain.repository

import android.net.Uri
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork

interface FileRepository {
    suspend fun saveFile(
        byteArray: ByteArray,
        fileName: String
    ): ResultWork<Uri, ChatDataError.FileErrorChat>

    suspend fun saveFileLocal(
        url: String,
        fileName: String,
        token: String,
    ): ResultWork<Unit, ChatDataError.FileErrorChat>
}