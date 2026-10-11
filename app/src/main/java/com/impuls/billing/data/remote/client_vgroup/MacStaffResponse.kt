package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MacStaffResponse(
    val macId: Long,      // Идентификатор записи о назначенном MAC-адресе в таблице MAC-адресов текущих сессий
    val recordId: Long,   // Идентификатор записи в БД
    val vgId: Long,       // Идентификатор учетной записи
    val segment: String,  // IP-подсеть или автономная сеть, назначенная учетной записи
    val mac: String       // Назначенный MAC-адрес
)
