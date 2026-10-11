package com.impuls.billing.data.remote.requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientNoticesRequest(
    @SerialName("notices")
    val notices: List<NoticesRequest>,
    @SerialName("uid")
    val uid: Int
)