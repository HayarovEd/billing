package com.impuls.chat.data.repository

import android.app.Application
import android.os.Build
import android.provider.Settings
import com.impuls.chat.domain.model.DeviceData
import com.impuls.chat.domain.repository.ServiceChatRepository
import java.util.Locale

internal class ServiceChatRepositoryImpl(
    private val application: Application
): ServiceChatRepository {

    override fun getDeviceData(): DeviceData {
        // Получаем системный ANDROID_ID
        val androidId = Settings.Secure.getString(
            application.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        // На случай, если по какой-то причине вернулся null (крайне редко)
        return DeviceData(
            deviceId = androidId ?: "unknown_device",
            deviceName = getDeviceName()
        )
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        return if (model.lowercase(Locale.getDefault()).startsWith(manufacturer.lowercase(Locale.getDefault()))) {
            // Если модель уже включает имя бренда (например, "Huawei P40"), возвращаем только модель
            capitalize(model)
        } else {
            // Иначе склеиваем бренд и модель (например, "Google" + "Pixel 8" = "Google Pixel 8")
            "${capitalize(manufacturer)} $model"
        }
    }

    private fun capitalize(str: String): String {
        if (str.isEmpty()) return str
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }
}