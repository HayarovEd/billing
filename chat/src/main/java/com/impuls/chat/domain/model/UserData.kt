package com.impuls.chat.domain.model

data class UserData(
    val userId: String,
    val name: String,
    val role: String,
    val email: String,
    val expired: Long,

)
