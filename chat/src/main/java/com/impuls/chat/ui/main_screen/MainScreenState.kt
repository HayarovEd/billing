package com.impuls.chat.ui.main_screen

import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Group

data class MainScreenState(
    val name: String = "",
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val operId: Long? = null,
    val group: Group? = null,
    val isLoading: Boolean = false,
    val phone: String = "",
    val message: String = "",
    val chat: Chat? = null,
    val userChats: List<Chat> = emptyList(),
)