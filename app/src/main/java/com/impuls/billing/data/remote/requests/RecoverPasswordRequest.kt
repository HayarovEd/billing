package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecoverPasswordRequest(
    @SerialName("login")
    val login: String,
    @SerialName("operId")
    val operId: Int,
    @SerialName("simpleRecover")
    val simpleRecover: Boolean,
    @SerialName("sync")
    val sync: Boolean,
    @SerialName("transport")
    val transport: Int
)