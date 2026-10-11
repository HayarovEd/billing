package com.impuls.chat.domain.model


data class User(
    val email: String,
    val id: String,
    val isDisabled: Boolean,
    val name: String,
    val role: Role
)
