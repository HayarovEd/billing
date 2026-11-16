package com.edurda77.download_install.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
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
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = 1001
            try {
                val fileUrl = workerParameters.inputData.getString(FILE_URL)
                    ?: return@withContext Result.failure(workDataOf(KEY_ERROR to DownloadDataError.Network.BAD_REQUEST))
                val fileName = workerParameters.inputData.getString(FILE_NAME)
                    ?: return@withContext Result.failure(workDataOf(KEY_ERROR to DownloadDataError.Network.BAD_REQUEST))

                val apkFilePath = context.pathToDownloadFile(fileName)
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15_000
                connection.readTimeout = 30_000
                connection.connect()

                val contentLength = connection.contentLength.toLong()
                
                if (contentLength > 0 && !checkAvailableSpace(contentLength)) {
                    throw Exception("Not enough space on device")
                }
                
                cleanupOldApks(fileName)

                val channelName = context.resources.getString(R.string.download_update)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(DOWNLOAD_UPDATE_CHANNEL, channelName, NotificationManager.IMPORTANCE_LOW)
                    notificationManager.createNotificationChannel(channel)
                }

                val builder = Builder(context, DOWNLOAD_UPDATE_CHANNEL)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setContentTitle(channelName)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)

                val data = ByteArray(8192)
                var totalDownloaded: Long = 0
                var count: Int
                var lastProgress = -1

                connection.inputStream.use { inputStream ->
                    FileOutputStream(apkFilePath).use { outputStream ->
                        while (inputStream.read(data).also { count = it } != -1) {
                            totalDownloaded += count.toLong()
                            outputStream.write(data, 0, count)
                            if (contentLength > 0) {
                                val progress = (totalDownloaded * 100 / contentLength).toInt()
                                if (progress > lastProgress) {
                                    lastProgress = progress
                                    builder
                                        .setContentText("$fileName: $progress%")
                                        .setProgress(100, progress, false)
                                    notificationManager.notify(notificationId, builder.build())
                                    setProgress(workDataOf(KEY_PROGRESS to progress))
                                }
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, context.resources.getString(R.string.download_finished), Toast.LENGTH_LONG).show()
                }

                builder
                    .setContentTitle(context.resources.getString(R.string.download_finished))
                    .setContentText(context.resources.getString(R.string.update_downloaded))
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setOngoing(false)
                    .setProgress(0, 0, false)
                notificationManager.notify(notificationId, builder.build())
                
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                val builder = Builder(context, DOWNLOAD_UPDATE_CHANNEL)
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setContentTitle(context.resources.getString(R.string.download_error))
                    .setContentText(e.message ?: context.resources.getString(R.string.unknown))
                    .setOngoing(false)
                notificationManager.notify(notificationId, builder.build())
                Result.failure(workDataOf(KEY_ERROR to DownloadDataError.Network.SERVER_ERROR))
            }
        }
    }

    private fun checkAvailableSpace(requiredBytes: Long): Boolean {
        val path = (context.externalCacheDir ?: context.cacheDir).absolutePath
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
            val dir = context.externalCacheDir ?: context.cacheDir
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
