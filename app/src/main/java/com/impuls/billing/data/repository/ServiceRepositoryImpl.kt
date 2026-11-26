package com.impuls.billing.data.repository

import android.app.Application
import com.edurda77.download_install.utils.getCurrentAppVersionName
import com.impuls.billing.domain.repository.ServiceRepository


class ServiceRepositoryImpl(private val application: Application) : ServiceRepository {

    override fun getVersionName(): String? {
        return application.getCurrentAppVersionName()
    }
}