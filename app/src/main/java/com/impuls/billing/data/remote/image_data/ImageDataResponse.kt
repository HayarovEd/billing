package com.impuls.billing.data.remote.image_data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDataResponse(
    @SerialName("result")
    val result: Result,
    @SerialName("status")
    val status: Int
)