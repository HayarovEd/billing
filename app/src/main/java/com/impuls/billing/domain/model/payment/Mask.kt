package com.impuls.billing.domain.model.payment


data class Mask(
    val editable: Boolean,
    val maskType: String,
    val maxLength: Int,
    val minLength: Int,
    val name: String,
    val note: String,
    val propertyType: String,
    val sortNumber: Int,
    val viewName: String,
    val visible: Boolean
)
