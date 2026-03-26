package com.impuls.billing.domain.model.payment


data class ReceiptCard(
    val createDate: String,
    val fiscalUrl: String,
    val fnsUrl: String?,
    val infoUrl: String,
    val regPayNum: String
)
