package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.Serializable

@Serializable
data class CurrentModifierResponse (
    val type: String, //Тип модификатора
    val value: Double, //Коэффициент скидки
)