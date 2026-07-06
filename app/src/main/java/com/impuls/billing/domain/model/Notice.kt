package com.impuls.billing.domain.model


data class Notice(
    val descr: String,
    val email: Boolean,
    val messenger: Boolean,
    val name: String,
    val sms: Boolean,
    val type: Int,
    val value: String
)