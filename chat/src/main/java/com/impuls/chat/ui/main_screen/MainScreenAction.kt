package com.impuls.chat.ui.main_screen


sealed interface MainScreenAction {
    class UpdateName(val name: String) : MainScreenAction
    class UpdateEmail(val email: String) : MainScreenAction
    class UpdatePhone(val phone: String) : MainScreenAction
    class UpdateMessage(val message: String) : MainScreenAction
    data object CreateChat: MainScreenAction
}