package com.impuls.chat.ui.main_screen

import com.impuls.chat.domain.model.Group

sealed interface MainScreenAction {
    class UpdateName(val name: String) : MainScreenAction
    class UpdateEmail(val email: String) : MainScreenAction
    class UpdatePhone(val phone: String) : MainScreenAction
    class UpdateMessage(val message: String) : MainScreenAction
    class UpdateTheme(val theme: Group) : MainScreenAction
    data object CreateChat: MainScreenAction
    data class OnChatClick(val chatId: String) : MainScreenAction
}