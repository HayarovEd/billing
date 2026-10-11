package com.impuls.billing.data.remote.payment_history


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayDto(
    @SerialName("amount")
    val amount: Double,
    @SerialName("cancelDate")
    val cancelDate: String,
    @SerialName("cashCode")
    val cashCode: Long,
    @SerialName("classId")
    val classId: Long,
    @SerialName("className")
    val className: String,
    @SerialName("comment")
    val comment: String,
    @SerialName("currId")
    val currId: Long,
    @SerialName("fromAgrmId")
    val fromAgrmId: Long,
    @SerialName("fromAgrmNumber")
    val fromAgrmNumber: String,
    @SerialName("localDate")
    val localDate: String,
    @SerialName("modPerson")
    val modPerson: Long,
    @SerialName("orderId")
    val orderId: Long,
    @SerialName("parentRecordId")
    val parentRecordId: Long,
    @SerialName("payDate")
    val payDate: String,
    @SerialName("paymentOrderNumber")
    val paymentOrderNumber: String,
    @SerialName("periodDate")
    val periodDate: String,
    @SerialName("receipt")
    val receipt: String,
    @SerialName("recordId")
    val recordId: Long,
    @SerialName("revNo")
    val revNo: Long,
    @SerialName("revisions")
    val revisions: Long,
    @SerialName("status")
    val status: Long,
    @SerialName("uuid")
    val uuid: String
)