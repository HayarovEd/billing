package com.impuls.billing.data.remote.image_data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("color")
    val color: String,
    @SerialName("image")
    val image: String
)