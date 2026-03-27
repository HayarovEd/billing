package com.impuls.billing.data.remote.payment.base_data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyPaymentResponse(
    @SerialName("amountMode")
    val amountMode: String,
    @SerialName("bankAccount")
    val bankAccount: String,
    @SerialName("bic")
    val bic: String,
    @SerialName("comissions")
    val comissionDtos: List<ComissionDto>,
    @SerialName("currency")
    val currency: String,
    @SerialName("dynCom")
    val dynCom: String,
    @SerialName("inn")
    val inn: String,
    @SerialName("kpp")
    val kpp: String,
    @SerialName("logoUrl")
    val logoUrl: String,
    @SerialName("masks")
    val maskDtos: List<MaskDto>,
    @SerialName("multiCurrency")
    val multiCurrency: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("offline")
    val offline: Boolean,
    @SerialName("orgCode")
    val orgCode: Int,
    @SerialName("orgName")
    val orgName: String,
    @SerialName("pfAds")
    val pfAds: String,
    @SerialName("readOnlyAmount")
    val readOnlyAmount: Boolean,
    @SerialName("receivingCurrency")
    val receivingCurrency: String,
    @SerialName("serviceCode")
    val serviceCode: String,
    @SerialName("serviceName")
    val serviceName: String,
    @SerialName("serviceType")
    val serviceType: String,
    @SerialName("tags")
    val tags: String
)