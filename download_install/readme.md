## Preview

Download and Install is an Android library for updating applications without using app markets.
It downloads APK files from a provided link and initiates the installation request for the user(in application or background).

## Getting started
Use Ktor

1. In Application class
- add 
```kt
fun onCreate() {
    super.onCreate()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        registerChannel()
    }
}
private fun registerChannel() {
  val channel = NotificationChannel(
  PRIMARY_CHANNEL, DOWNLOAD_UPDATE_CHANNEL, NotificationManager.IMPORTANCE_LOW
  )
  //channel.lightColor = Color.GREEN
  //channel.lockscreenVisibility = Notification.VISIBILITY_SECRET
  (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
  }
```

2. DI
2.1 Koin
 ```kt
internal val libModule = module {
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
        DownloadProvider.provideRefresher(application = get(), downloadRepository = get(), installer = get())
    }
    worker { DownloadUpdateWorker(notificationBuilder = get(), application = get(), workerParameters =  get()) }
}
```
