package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeRequest(
    @SerialName("code")
    val code: String,
    @SerialName("id")
    val id: Long
)