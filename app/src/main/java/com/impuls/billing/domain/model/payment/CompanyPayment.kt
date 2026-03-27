package com.impuls.billing.domain.model.payment


data class CompanyPayment (
    val amountMode: String,
    val bankAccount: String,
    val bic: String,
    val comissions: List<Comission>,
    val currency: String,
    val dynCom: String,
    val inn: String,
    val kpp: String,
    val logoUrl: String,
    val maskDtos: List<Mask>,
    val multiCurrency: Boolean,
    val name: String,
    val offline: Boolean,
    val orgCode: Int,
    val orgName: String,
    val pfAds: String,
    val readOnlyAmount: Boolean,
    val receivingCurrency: String,
    val serviceCode: String,
    val serviceName: String,
    val serviceType: String,
    val tags: String
)