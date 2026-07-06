package com.impuls.chat.domain.utils

import android.util.Base64
import com.impuls.chat.domain.model.UserData
import org.json.JSONObject

internal object JwtRepository {
    fun decodeJwt(token: String): UserData? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = decodeBase64(parts[1])
            val jsonObject = JSONObject(payload)
            UserData(
                userId = jsonObject.getString("sub"),
                expired = jsonObject.getLong("exp"),
                name = jsonObject.getString("name"),
                role = jsonObject.getString("role"),
                email = jsonObject.getString("email")
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