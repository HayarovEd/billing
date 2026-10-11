package com.impuls.billing.domain.model


data class ClientInfo(
    val email: String,
    val emailIsConfirmed: Boolean,
    val kontPerson: String,
    val mobile: String,
    val mobileIsConfirmed: Boolean,
    val offerIsAccepted: Boolean,
    val passIsTemporary: Boolean,
    val password: String,
    val phone: String,
    val triggerContactDataReminder: Boolean
)
