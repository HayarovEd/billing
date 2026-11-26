package com.impuls.billing.di

import androidx.core.app.NotificationCompat
import com.edurda77.download_install.DownloadProvider
import com.edurda77.download_install.downloader.DownloadRepository
import com.edurda77.download_install.installer.Installer
import com.edurda77.download_install.model.UpdateConfig
import com.edurda77.download_install.refresher.Refresher
import com.edurda77.download_install.worker.CheckUpdateWorker
import com.edurda77.download_install.worker.DownloadUpdateWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

const val DOWNLOAD_VERSION_URL =
    "https://apps.kvadroks.ru/api/links/7ef64b79-d5e5-4399-a30c-8d0653ed8d8b/version"

const val DOWNLOAD_FILE_URL =
    "https://apps.kvadroks.ru/api/links/7ef64b79-d5e5-4399-a30c-8d0653ed8d8b/file"

val downloadUpdateModule = module {
    single {
        UpdateConfig(
            versionUrl = DOWNLOAD_VERSION_URL,
            downloadUrl = DOWNLOAD_FILE_URL,
            downloadedFileName = "billing_update.apk",
            checkIntervalHours = 1L
        )
    }
    single<DownloadRepository> {
        DownloadProvider.provideDownloader(httpClient = get(), application = get())
    }
    single<Installer> {
        DownloadProvider.provideInstaller(application = get())
    }
    single<NotificationCompat.Builder> {
        DownloadProvider.provideNotificationBuilder(application = get())
    }
    single<Refresher> {
        DownloadProvider.provideRefresher(
            application = get(),
            downloadRepository = get(),
            installer = get()
        )
    }
    worker {
        DownloadUpdateWorker(
            context = get(),
            workerParameters = get()
        )
    }
    worker {
        CheckUpdateWorker(
            context = get(),
            workerParameters = get()
        )
    }
}
