package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    @SerialName("email")
    val email: String,
    @SerialName("uid")
    val uid: Long
)