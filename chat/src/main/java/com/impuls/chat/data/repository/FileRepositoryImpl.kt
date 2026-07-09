package com.impuls.chat.data.repository

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import com.impuls.chat.domain.repository.FileRepository
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import java.io.File

class FileRepositoryImpl(
    private val application: Application
) : FileRepository {

    override suspend fun saveFile(
        byteArray: ByteArray,
        fileName: String,
    ): ResultWork<Uri, ChatDataError.FileErrorChat> {
        return try {
            val path = File("${application.cacheDir}")
            val file = File(path, fileName)
            file.writeBytes(byteArray)
            val uri = FileProvider.getUriForFile(
                application,
                "${application.packageName}.provider",
                file
            )
            ResultWork.Success(uri)
        } catch (_: Exception) {
            return ResultWork.Error(ChatDataError.FileErrorChat.WRITE_ERROR)
        }
    }
}