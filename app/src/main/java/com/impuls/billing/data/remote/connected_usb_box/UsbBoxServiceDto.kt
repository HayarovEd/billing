package com.impuls.billing.data.remote.connected_usb_box


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsbBoxServiceDto(
    @SerialName("activated")
    val activated: String,
    @SerialName("blockdiscount")
    val blockdiscount: Double,
    @SerialName("blocked")
    val blocked: Long,
    @SerialName("catidx")
    val catidx: Long,
    @SerialName("comment")
    val comment: String,
    @SerialName("created")
    val created: String,
    @SerialName("discount")
    val discount: Double,
    @SerialName("discounttimefrom")
    val discounttimefrom: String,
    @SerialName("discounttimeto")
    val discounttimeto: String,
    @SerialName("externaldata")
    val externaldata: String,
    @SerialName("installmentduration")
    val installmentduration: Long,
    @SerialName("installmentfirstpayment")
    val installmentfirstpayment: Double,
    @SerialName("mul")
    val mul: Double,
    @SerialName("needcalc")
    val needcalc: Long,
    @SerialName("noduplicate")
    val noduplicate: Long,
    @SerialName("notusbox")
    val notusbox: Long,
    @SerialName("personid")
    val personid: String,
    @SerialName("planid")
    val planid: Long,
    @SerialName("rate")
    val rate: Double,
    @SerialName("servid")
    val servid: Long,
    @SerialName("state")
    val state: Long,
    @SerialName("tarid")
    val tarid: Long,
    @SerialName("timefrom")
    val timefrom: String,
    @SerialName("timeto")
    val timeto: String,
    @SerialName("vgid")
    val vgid: Long
)