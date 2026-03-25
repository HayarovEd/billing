package com.impuls.billing.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import com.impuls.billing.domain.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ServiceRepositoryImpl (
    private val application: Application
): ServiceRepository {

    override suspend fun saveBase64ToFile(base64Data: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val base64String = if (base64Data.contains(",")) {
                    base64Data.substringAfter(",")
                } else {
                    base64Data
                }
                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                // Создаем временный файл
                val file = File(application.cacheDir, "image_${System.currentTimeMillis()}.png")
                FileOutputStream(file).use { outStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                }

                // Получаем URI через FileProvider
                FileProvider.getUriForFile(
                    application,
                    "${application.packageName}.fileprovider",
                    file
                )
            } catch (_: Exception) {
                null
            }
        }

    }
}