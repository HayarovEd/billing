package com.impuls.billing.data.remote.payment.base_data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaskDto(
    @SerialName("editable")
    val editable: Boolean,
    @SerialName("maskType")
    val maskType: String,
    @SerialName("maxLength")
    val maxLength: Int,
    @SerialName("minLength")
    val minLength: Int,
    @SerialName("name")
    val name: String,
    @SerialName("note")
    val note: String,
    @SerialName("propertyType")
    val propertyType: String,
    @SerialName("sortNumber")
    val sortNumber: Int,
    @SerialName("viewName")
    val viewName: String,
    @SerialName("visible")
    val visible: Boolean
)