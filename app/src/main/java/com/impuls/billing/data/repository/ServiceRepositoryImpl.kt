package com.impuls.billing.data.repository

import android.app.Application
import com.impuls.billing.domain.repository.ServiceRepository


class ServiceRepositoryImpl(private val application: Application) : ServiceRepository {

    override fun getVersionName(): Double? {
        // val a = application.externalCacheDir?.absolutePath
        return application.packageManager.getPackageInfo(
            application.packageName,
            0
        ).versionName?.toDoubleOrNull()
    }
}