package com.impuls.chat.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationChatRoute {

    @Serializable
    data class MainRoute(
        val name: String = "Test App",
        val login:String = "testapp",
        val password: String = "ut82fph38u",
        val email: String = "",
        val groupName: String = "Группа 15",
    ): NavigationChatRoute()
    @Serializable
    data class ChatRoute(
        val chatId: String,
        val name: String,
        val login:String,
        val password: String,
        val email: String,
    ): NavigationChatRoute()
}