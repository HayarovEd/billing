package com.impuls.chat.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("message")
    val message: String? = null,
    @SerialName("error")
    val error: String? = null,
    @SerialName("detail")
    val detail: String? = null
)