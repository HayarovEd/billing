package com.edurda77.download_install.downloader.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppResponseDto(
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)