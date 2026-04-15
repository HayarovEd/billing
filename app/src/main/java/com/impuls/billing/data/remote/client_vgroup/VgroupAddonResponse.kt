package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.Serializable

@Serializable
data class VgroupAddonResponse(
    /**
     * Идентификатор учётной записи
     */
    val vgId: Long,

    /**
     * Тип поля
     *
     * Возможные значения:
     * * 0 — текстовое
     * * 1 — список значений
     * * 2 — логический тип
     * * 3 — дата
     */
    val type: Long,

    /**
     * Идентификатор агента
     */
    val agentId: Long,

    /**
     * Идентификатор значения из списка, если type = 1
     */
    val idx: Long,

    /**
     * Имя дополнительного поля
     */
    val name: String,

    /**
     * Описание дополнительного поля
     */
    val descr: String,

    /**
     * Значение поля, если type = 0
     */
    val strValue: String
)
