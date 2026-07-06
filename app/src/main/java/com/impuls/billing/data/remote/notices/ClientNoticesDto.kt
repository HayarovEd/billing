package com.impuls.billing.data.remote.notices


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientNoticesDto(
    @SerialName("notices")
    val notices: List<NoticeDto>,
    @SerialName("uid")
    val uid: Int
)