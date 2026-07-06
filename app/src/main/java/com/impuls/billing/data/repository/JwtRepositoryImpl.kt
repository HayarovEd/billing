package com.impuls.billing.data.repository

import android.util.Base64
import com.impuls.billing.domain.model.UserData
import com.impuls.billing.domain.repository.JwtRepository
import org.json.JSONObject

class JwtRepositoryImpl: JwtRepository {
    override  fun decodeJwt(token: String): UserData? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = decodeBase64(parts[1])
            val jsonObject = JSONObject(payload)
            UserData(
                userId = jsonObject.getLong("sub"),
                expired = jsonObject.getLong("exp")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decodeBase64(encoded: String): String {
        val decodedBytes = Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_PADDING)
        return String(decodedBytes, Charsets.UTF_8)
    }
}