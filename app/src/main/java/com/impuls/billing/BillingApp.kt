package com.impuls.billing

import android.app.Application
import androidx.work.Configuration
import com.impuls.billing.di.baseModule
import com.impuls.billing.di.downloadUpdateModule
import com.impuls.billing.di.repoModule
import com.impuls.billing.di.viewModelModule
import com.impuls.chat.di.repoChatModule
import com.impuls.chat.di.viewModelChatModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class BillingApp : Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(KoinWorkerFactory())
            .build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BillingApp)
            modules(
                baseModule,
                viewModelModule,
                repoModule,
                downloadUpdateModule,
                viewModelChatModule,
                repoChatModule
            )
            workManagerFactory()
        }
    }
}