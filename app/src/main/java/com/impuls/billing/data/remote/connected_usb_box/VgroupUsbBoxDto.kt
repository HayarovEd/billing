package com.impuls.billing.data.remote.connected_usb_box


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VgroupUsbBoxDto(
    @SerialName("acconDate")
    val acconDate: String,
    @SerialName("agrmId")
    val agrmId: Long,
    @SerialName("agrmNumber")
    val agrmNumber: String,
    @SerialName("assignedPromisePayment")
    val assignedPromisePayment: Boolean,
    @SerialName("autoAssign")
    val autoAssign: Long,
    @SerialName("available")
    val available: Long,
    @SerialName("availableForManager")
    val availableForManager: Long,
    @SerialName("catAbove")
    val catAbove: Double,
    @SerialName("catDescr")
    val catDescr: String,
    @SerialName("common")
    val common: Long,
    @SerialName("currentModifier")
    val currentModifier: UsbBoxCurrentModifierDto,
    @SerialName("includeAbove")
    val includeAbove: Double,
    @SerialName("personName")
    val personName: String,
    @SerialName("service")
    val service: UsbBoxServiceDto,
    @SerialName("serviceCatId")
    val serviceCatId: Long,
    @SerialName("smartCardId")
    val smartCardId: Long,
    @SerialName("smartCardSerial")
    val smartCardSerial: String,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("tarDescr")
    val tarDescr: String,
    @SerialName("tarType")
    val tarType: Long,
    @SerialName("technicalService")
    val technicalService: Long,
    @SerialName("totalAmount")
    val totalAmount: Double,
    @SerialName("uid")
    val uid: Long,
    @SerialName("used")
    val used: Long,
    @SerialName("username")
    val username: String,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("vgLogin")
    val vgLogin: String
)