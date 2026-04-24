package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientInfoDto(
    @SerialName("email")
    val email: String,
    @SerialName("emailIsConfirmed")
    val emailIsConfirmed: Boolean,
    @SerialName("kontPerson")
    val kontPerson: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("mobileIsConfirmed")
    val mobileIsConfirmed: Boolean,
    @SerialName("offerIsAccepted")
    val offerIsAccepted: Boolean,
    @SerialName("passIsTemporary")
    val passIsTemporary: Boolean,
    @SerialName("password")
    val password: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("triggerContactDataReminder")
    val triggerContactDataReminder: Boolean
)