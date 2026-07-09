package com.impuls.chat.data.repository

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.impuls.chat.domain.repository.FileRepository
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import java.io.File
import androidx.core.net.toUri
import com.impuls.chat.domain.utils.BASE_CHAT_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileRepositoryImpl(
    private val application: Application
) : FileRepository {

    private val downloadManager =
        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override suspend fun saveFile(
        byteArray: ByteArray,
        fileName: String,
    ): ResultWork<Uri, ChatDataError.FileErrorChat> {
        return withContext(Dispatchers.IO) {
            try {
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
                ResultWork.Error(ChatDataError.FileErrorChat.WRITE_ERROR)
            }
        }
    }

    override suspend fun saveFileLocal(
        url: String,
        fileName: String,
        token: String,
    ): ResultWork<Unit, ChatDataError.FileErrorChat> {
        return withContext(Dispatchers.IO) {
            try {
                val request = DownloadManager.Request((BASE_CHAT_URL + "files/" + url).toUri())
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).apply {
                        addRequestHeader("Authorization", "Bearer $token")
                    }

                downloadManager.enqueue(request)
                ResultWork.Success(Unit)
            } catch (_: Exception) {
                ResultWork.Error(ChatDataError.FileErrorChat.WRITE_ERROR)
            }
        }
    }
}