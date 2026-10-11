package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaffResponse (
    val recordId: Long,           // Идентификатор записи из БД
    val vgId: Long,               // Идентификатор учётной записи
    val equipId: Long,            // Идентификатор абонентского устройства
    val type: Long,               // Тип сегмента сети: 0 - IP-подсеть, 1 - IP-подсеть с привязкой к порту, 2 - автономная сеть
    val asId: Long,               // Идентификатор автономной сети (as - ключевое слово, нужны обратные кавычки)
    val servId: Long,             // Услуга, которая тарифицирует привязанный IP
    val servcatIdx: Long,         // Идентификатор категории тарифа
    val createService: Boolean,   // Флаг "Создать услугу автоматически"
    val ipMask: IPMaskResponse            // Информация об IP-адресе и маске
)