package com.impuls.chat.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushTokenRequest (
    @SerialName("token") val token: String,
    @SerialName("device_name") val deviceName: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("platform") val platform: String,
)
