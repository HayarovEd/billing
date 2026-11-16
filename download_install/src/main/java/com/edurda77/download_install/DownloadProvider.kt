package com.edurda77.download_install

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.edurda77.download_install.downloader.DownloadRepository
import com.edurda77.download_install.downloader.DownloadRepositoryImpl
import com.edurda77.download_install.installer.Installer
import com.edurda77.download_install.installer.InstallerImpl
import com.edurda77.download_install.refresher.Refresher
import com.edurda77.download_install.refresher.RefresherImpl
import com.edurda77.download_install.utils.DOWNLOAD_UPDATE_CHANNEL
import io.ktor.client.HttpClient

object DownloadProvider {


   /* internal fun provideHttpClient(): HttpClient =
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                connectTimeoutMillis = 100000
                requestTimeoutMillis = 100000
                socketTimeoutMillis = 100000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
*/

    fun provideDownloader(
        httpClient: HttpClient,
        application: Application
    ): DownloadRepository =
        DownloadRepositoryImpl(
            httpClient = httpClient,
            application = application
        )


    fun provideInstaller(application: Application): Installer =
        InstallerImpl(application)

    fun provideNotificationBuilder(application: Application): NotificationCompat.Builder {
        val channelId = DOWNLOAD_UPDATE_CHANNEL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = application.getString(R.string.download_update)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(application, channelId)
            .setSmallIcon(R.drawable.outline_update_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(false)
            .setAutoCancel(true)
    }

    fun provideRefresher(
        application: Application,
        downloadRepository: DownloadRepository,
        installer: Installer
    ): Refresher =
        RefresherImpl(
            application = application,
            downloadRepository = downloadRepository,
            installer = installer
        )
}
