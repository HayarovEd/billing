package com.impuls.chat.ui.main_screen

import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.SubGroup

data class MainScreenState(
    val name: String = "",
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val groupName: String = "",
    val themes: List<SubGroup> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTheme: SubGroup? = null,
    val phone: String = "",
    val message: String = "",
    val chat: Chat? = null,
    val userChats: List<Chat> = emptyList(),
)