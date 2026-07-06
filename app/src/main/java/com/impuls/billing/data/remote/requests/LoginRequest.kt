package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String
)