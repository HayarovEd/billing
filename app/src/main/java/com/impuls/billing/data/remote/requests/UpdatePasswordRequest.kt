package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordRequest(
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("oldPassword")
    val oldPassword: String
)