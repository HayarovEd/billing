package com.impuls.chat.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest (
    @SerialName("token")
    val token: String
)