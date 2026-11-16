package com.edurda77.download_install.worker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
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
            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = 1001
            try {
                val fileUrl = workerParameters.inputData.getString(FILE_URL)
                val fileName = workerParameters.inputData.getString(FILE_NAME)
                val apkFilePath = application.pathToDownloadFile("$fileName")
                val outputStream = FileOutputStream(apkFilePath)
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val contentLength = connection.contentLength.toLong()
                
                if (contentLength > 0 && !checkAvailableSpace(contentLength)) {
                    throw Exception("Not enough space on device")
                }
                
                cleanupOldApks(fileName ?: "")

                val inputStream = connection.inputStream

                val data = ByteArray(1024)
                var totalDownloaded: Long = 0
                var count: Int
                var lastProgress = -1

                val channelName = application.resources.getString(R.string.download_update)
                val channel = NotificationChannel(DOWNLOAD_UPDATE_CHANNEL, channelName, NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(channel)

                val builder = Builder(application, DOWNLOAD_UPDATE_CHANNEL)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setContentTitle(channelName)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)

                while (inputStream.read(data).also { count = it } != -1) {
                    totalDownloaded += count.toLong()
                    outputStream.write(data, 0, count)
                    if (contentLength > 0) {
                        val progress = (totalDownloaded * 100 / contentLength).toInt()
                        if (progress > lastProgress) {
                            lastProgress = progress
                            builder
                                .setContentText("${fileName}: $progress%")
                                .setProgress(100, progress, false)
                            notificationManager.notify(notificationId, builder.build())
                            setProgress(workDataOf(KEY_PROGRESS to progress))
                        }
                    }
                }
                inputStream.close()
                outputStream.close()
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(application, application.resources.getString(R.string.download_finished), Toast.LENGTH_LONG).show()
                }

                builder
                    .setContentTitle(application.resources.getString(R.string.download_finished))
                    .setContentText(application.resources.getString(R.string.update_downloaded))
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setOngoing(false)
                    .setProgress(0, 0, false)
                notificationManager.notify(notificationId, builder.build())
                
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                val builder = Builder(application, DOWNLOAD_UPDATE_CHANNEL)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setContentTitle(application.resources.getString(R.string.download_error))
                    .setContentText(e.message)
                    .setOngoing(false)
                notificationManager.notify(notificationId, builder.build())
                Result.failure(workDataOf(KEY_ERROR to DownloadDataError.Network.SERVER_ERROR))
            }
        }
    }

    private fun checkAvailableSpace(requiredBytes: Long): Boolean {
        val path = application.externalCacheDir?.absolutePath ?: application.cacheDir.absolutePath
        return try {
            val stat = android.os.StatFs(path)
            val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
            availableBytes > requiredBytes + (20 * 1024 * 1024) // 20MB safety buffer
        } catch (e: Exception) {
            true
        }
    }

    private fun cleanupOldApks(currentFileName: String) {
        try {
            val dir = application.externalCacheDir ?: application.cacheDir
            dir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".apk") && file.name != currentFileName) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }
}
