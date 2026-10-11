package com.impuls.chat.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationChatRoute {

    @Serializable
    data class MainRoute(
        val name: String,
        val login:String,
        val email: String,
        val operId: Long,
    ): NavigationChatRoute()
    @Serializable
    data class ChatRoute(
        val chatId: String,
    ): NavigationChatRoute()
}