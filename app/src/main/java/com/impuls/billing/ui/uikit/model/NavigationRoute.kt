package com.impuls.billing.ui.uikit.model

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object LoginRoute : NavigationRoute()

    @Serializable
    data object SplashRoute : NavigationRoute()

    @Serializable
    data object ProfileRoute : NavigationRoute()

    @Serializable
    data class PaymentRoute(
        val number: String,
        val operId: Long,
    ) : NavigationRoute()

    @Serializable
    data class HomeRoute(
        val agrmId: Long
    ) : NavigationRoute()

    @Serializable
    data class HistoryPaymentRoute(
        val agrmId: Long,
        val vgId: Long,
        val tariffId: Long,
    ) : NavigationRoute()

    @Serializable
    data class SettingsRoute(
        val userId: Long,
    ) : NavigationRoute()

    @Serializable
    data class BlockingRoute(
        val vgId: Long,
    ) : NavigationRoute()

    @Serializable
    data class PromiseRoute(
        val agrmId: Long
    ) : NavigationRoute()
   /* @Serializable
    data class MainRoute(
        val name: String = "Test App",
        val login:String = "testapp",
        val password: String = "ut82fph38u",
        val email: String = "",
        val groupName: String = "Группа 15",
    ): NavigationRoute()
    @Serializable
    data class ChatRoute(
        val chatId: String,
        val name: String,
        val login:String,
        val password: String,
        val email: String,
    ): NavigationRoute()
*/
}