package com.impuls.billing.domain.repository

import android.net.Uri

interface ServiceRepository {
    suspend fun saveBase64ToFile(base64Data: String): Uri?
}