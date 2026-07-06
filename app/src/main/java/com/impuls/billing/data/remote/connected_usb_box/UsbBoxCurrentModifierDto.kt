package com.impuls.billing.data.remote.connected_usb_box


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsbBoxCurrentModifierDto(
    @SerialName("type")
    val type: String,
    @SerialName("value")
    val value: Double
)