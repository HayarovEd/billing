package com.impuls.billing.data.remote.payment.base_data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComissionDto(
    @SerialName("comissionType")
    val comissionType: String,
    @SerialName("maxAmount")
    val maxAmount: Long?,
    @SerialName("maxPayAmount")
    val maxPayAmount: Int,
    @SerialName("minAmount")
    val minAmount: Long?,
    @SerialName("minPayAmount")
    val minPayAmount: Int,
    @SerialName("payCurrency")
    val payCurrency: String,
    @SerialName("payType")
    val payType: String,
    @SerialName("term")
    val termDto: TermDto,
    @SerialName("value")
    val value: String
)