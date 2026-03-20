package com.impuls.billing

import android.app.Application
import com.impuls.billing.di.baseModule
import com.impuls.billing.di.repoModule
import com.impuls.billing.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BillingApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BillingApp)
            modules(
                baseModule, viewModelModule, repoModule
            )
        }
    }
}