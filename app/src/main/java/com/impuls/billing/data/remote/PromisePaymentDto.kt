package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromisePaymentDto(
    @SerialName("agrmId")
    val agrmId: Int,
    @SerialName("agrmNum")
    val agrmNum: String,
    @SerialName("amount")
    val amount: Double,
    @SerialName("cancelFio")
    val cancelFio: String,
    @SerialName("cancelPerson")
    val cancelPerson: Int,
    @SerialName("currId")
    val currId: Int,
    @SerialName("debt")
    val debt: Double,
    @SerialName("fee")
    val fee: Double,
    @SerialName("modFio")
    val modFio: String,
    @SerialName("modPerson")
    val modPerson: Int,
    @SerialName("payId")
    val payId: Int,
    @SerialName("promiseDate")
    val promiseDate: String,
    @SerialName("promiseTill")
    val promiseTill: String,
    @SerialName("recordId")
    val recordId: Int,
    @SerialName("status")
    val status: Int,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("username")
    val username: String
)