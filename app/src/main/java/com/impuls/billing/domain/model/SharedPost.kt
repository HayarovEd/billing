package com.impuls.billing.domain.model

import kotlinx.datetime.LocalDateTime


data class SharedPost(
    val groupId: Int,
    val pid: Int,
    val postCatId: Int,
    val postTime: LocalDateTime,
    val status: Int,
    val subject: String,
    val text: String,
    val uid: Int
)
