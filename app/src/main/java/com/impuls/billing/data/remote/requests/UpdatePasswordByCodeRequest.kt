package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordByCodeRequest(
    @SerialName("code")
    val code: String,
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String
)