package com.impuls.billing.data.remote.payment_sberpay


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReceiptSberDto(
    @SerialName("createDate")
    val createDate: String,
    @SerialName("fiscalUrl")
    val fiscalUrl: String,
    @SerialName("fnsUrl")
    val fnsUrl: String?,
    @SerialName("infoUrl")
    val infoUrl: String,
    @SerialName("regPayNum")
    val regPayNum: String
)