package com.impuls.billing.domain.model


data class ClientNotices(
    val notices: List<Notice>,
    val uid: Int
)