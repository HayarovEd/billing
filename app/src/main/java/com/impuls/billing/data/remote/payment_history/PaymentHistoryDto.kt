package com.impuls.billing.data.remote.payment_history


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentHistoryDto(
    @SerialName("agrm")
    val agrm: String,
    @SerialName("amountCurr")
    val amountCurr: Double,
    @SerialName("bsoDoc")
    val bsoDoc: Long,
    @SerialName("cardNumber")
    val cardNumber: Long,
    @SerialName("currSymb")
    val currSymb: String,
    @SerialName("eps")
    val eps: Boolean,
    @SerialName("localtimestamp")
    val localtimestamp: Long,
    @SerialName("login")
    val login: String,
    @SerialName("mgr")
    val mgr: String,
    @SerialName("mgrDescr")
    val mgrDescr: String,
    @SerialName("mgrLogin")
    val mgrLogin: String,
    @SerialName("operId")
    val operId: Long,
    @SerialName("operName")
    val operName: String,
    @SerialName("orderNum")
    val orderNum: String,
    @SerialName("pay")
    val payDto: PayDto,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("uid")
    val uid: Long,
    @SerialName("uname")
    val uname: String
)