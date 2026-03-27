package com.impuls.billing.data.remote.check_status_payment


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckPaymentStatusResponse(
    @SerialName("amount")
    val amount: Int,
    @SerialName("createDate")
    val createDate: String,
    @SerialName("providerName")
    val providerName: String,
    @SerialName("regPayNum")
    val regPayNum: String,
    @SerialName("state")
    val state: String,
    @SerialName("term")
    val term: TermDto
)