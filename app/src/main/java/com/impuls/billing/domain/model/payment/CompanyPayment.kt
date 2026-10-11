package com.impuls.billing.domain.model.payment



data class CompanyPayment (
    val amountMode: String,
    val commissions: List<Comission>,
    val currency: String,
    val dynCom: String,
    val masks: List<Mask>,
    val multiCurrency: Boolean,
    val name: String,
    val offline: Boolean,
    val pfAds: String,
    val readOnlyAmount: Boolean,
    val receivingCurrency: String,
    val serviceCode: String,
    val serviceName: String,
    val serviceType: String,
    val tags: String
)