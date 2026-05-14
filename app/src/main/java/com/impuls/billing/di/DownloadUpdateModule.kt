package com.impuls.billing.di

import androidx.core.app.NotificationCompat
import com.edurda77.download_install.DownloadProvider
import com.edurda77.download_install.downloader.DownloadRepository
import com.edurda77.download_install.installer.Installer
import com.edurda77.download_install.refresher.Refresher
import com.edurda77.download_install.worker.DownloadUpdateWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val downloadUpdateModule = module {
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
            notificationBuilder = get(),
            application = get(),
            workerParameters = get()
        )
    }
}