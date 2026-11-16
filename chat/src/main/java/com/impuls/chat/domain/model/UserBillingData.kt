package com.impuls.chat.domain.model

data class UserBillingData (
    val name: String,
    val login:String,
    val password: String,
    val email: String,
    val billingToken: String,
)