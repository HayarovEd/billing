package com.impuls.chat.data.repository

import android.content.Context
import com.google.android.gms.common.ConnectionResult as GmsConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.impuls.chat.domain.model.PushProvider
import com.huawei.hms.api.ConnectionResult as HmsConnectionResult

object PushAvailabilityChecker {

    fun getAvailableProvider(context: Context): PushProvider {
        // 1. Проверяем Google Play Сервисы
        val gmsAvailable = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == GmsConnectionResult.SUCCESS

        // 2. Проверяем Huawei Сервисы
        val hmsAvailable = HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(context) == HmsConnectionResult.SUCCESS

        return when {
            gmsAvailable -> PushProvider.FCM // Всегда в приоритете, если есть оба
            hmsAvailable -> PushProvider.HMS
            else -> PushProvider.NONE
        }
    }
}