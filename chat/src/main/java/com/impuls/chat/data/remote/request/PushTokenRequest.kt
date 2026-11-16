package com.impuls.chat.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushTokenRequest (
    @SerialName("token") val token: String,
    @SerialName("deviceName") val deviceName: String,
    @SerialName("deviceId") val deviceId: String,
    @SerialName("platform") val platform: String,
)