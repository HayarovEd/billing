package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IPMaskResponse(
    val ip: String,           // Назначенный IP-адрес
    val mask: Int,           // Маска подсети (unsignedInt)
    val prefix: Int,         // Длина префикса подсети (unsignedInt)
    val segmentId: Long       // Идентификатор сегмента подсети из БД
)
