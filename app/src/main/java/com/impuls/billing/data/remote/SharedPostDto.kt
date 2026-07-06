package com.impuls.billing.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedPostDto(
    @SerialName("dtFrom")
    val dtFrom: String,
    @SerialName("dtTo")
    val dtTo: String,
    @SerialName("emails")
    val emails: String,
    @SerialName("externUrl")
    val externUrl: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("pid")
    val pid: Int,
    @SerialName("postBy")
    val postBy: Int,
    @SerialName("postCatId")
    val postCatId: Int,
    @SerialName("postTime")
    val postTime: String,
    @SerialName("postTo")
    val postTo: Int,
    @SerialName("sentTime")
    val sentTime: String,
    @SerialName("status")
    val status: Int,
    @SerialName("subject")
    val subject: String,
    @SerialName("text")
    val text: String,
    @SerialName("uid")
    val uid: Int
)