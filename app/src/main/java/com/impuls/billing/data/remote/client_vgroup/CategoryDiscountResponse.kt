package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDiscountResponse(
    val catIdx: Long,           // Идентификатор тарифной категории
    val discount: Double,       // Коэффициент назначенной скидки
    val above: Double,          // Стоимость единицы трафика (мегабайта, минуты)
    val rate: Double,           // Величина скидки в процентах
    val includes: Long,         // Количество трафика, включенного в тарифную категорию
    val parentRecordId: Long,   // Идентификатор родительской учетной записи
    val keepModifier: Long      // Флаг переноса скидки: 0 - не переносить, 1 - переносить
)