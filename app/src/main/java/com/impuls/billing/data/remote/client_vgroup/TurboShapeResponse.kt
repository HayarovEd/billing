package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TurboShapeResponse(
    /**
     * Идентификатор записи
     */
    val recordId: Long,

    /**
     * Идентификатор учётной записи
     */
    val vgId: Long,

    /**
     * Идентификатор услуги USBOX
     */
    val servId: Long,

    /**
     * Идентификатор тарифа, в рамках которого действует скорость
     */
    val tarId: Long,

    /**
     * Идентификатор категории тарифа
     */
    val catIdx: Long,

    /**
     * Значение полосы пропускания
     */
    val shape: Long,

    /**
     * Время начала увеличения скорости
     */
    val timeFrom: String,

    /**
     * Время окончания увеличения скорости
     */
    val timeTo: String,

    /**
     * Описание категории тарифа
     */
    val descr: String,

    /**
     * Логин учётной записи
     */
    val login: String
)
