package com.edurda77.download_install.worker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.Builder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.edurda77.download_install.R
import com.edurda77.download_install.utils.DOWNLOAD_UPDATE_CHANNEL
import com.edurda77.download_install.utils.DownloadDataError
import com.edurda77.download_install.utils.FILE_NAME
import com.edurda77.download_install.utils.FILE_URL
import com.edurda77.download_install.utils.KEY_ERROR
import com.edurda77.download_install.utils.KEY_PROGRESS
import com.edurda77.download_install.utils.pathToDownloadFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadUpdateWorker(
    private val notificationBuilder: Builder,
    private val application: Application,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(application, workerParameters) {



    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val fileUrl = workerParameters.inputData.getString(FILE_URL)
                val fileName = workerParameters.inputData.getString(FILE_NAME)
                val apkFilePath =
                    application.pathToDownloadFile("$fileName")
                val outputStream = FileOutputStream(apkFilePath)
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val contentLength = connection.contentLength
                val inputStream = connection.inputStream

                val data = ByteArray(1024)
                var totalDownloaded: Long = 0
                var count: Int

                while (inputStream.read(data).also { count = it } != -1) {
                    totalDownloaded += count.toLong()
                    outputStream.write(data, 0, count)
                    if (contentLength > 0) {
                        val progress = (totalDownloaded * 100 / contentLength).toInt()
                        notificationBuilder.setChannelId(DOWNLOAD_UPDATE_CHANNEL)
                        notificationBuilder
                            .setContentTitle(application.resources.getString(R.string.download_update))
                            .setContentText("${fileName}: $progress%")
                        createNotificationManager("${fileName}: $progress%")
                            .notify(0, notificationBuilder.build())
                        setProgress(workDataOf(KEY_PROGRESS to progress))
                    }
                }
                inputStream.close()
                createNotificationManager(application.resources.getString(R.string.update_downloaded))
                    .notify(0, notificationBuilder.build())
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                createNotificationManager("${application.resources.getString(R.string.download_error)}: $e")
                    .notify(0, notificationBuilder.build())
                Result.failure(workDataOf(KEY_ERROR to DownloadDataError.Network.SERVER_ERROR))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationManager(
        content: String
    ): NotificationManager {
        val name = application.resources.getString(R.string.download_update)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(DOWNLOAD_UPDATE_CHANNEL, name, importance).apply {
            description = content
        }
        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }
}