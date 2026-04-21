package com.impuls.billing.ui.uikit.model

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object Login : NavigationRoute()

    @Serializable
    data object Splash : NavigationRoute()

    @Serializable
    data object Profile : NavigationRoute()

    @Serializable
    data class Payment(
        val number: String
    ) : NavigationRoute()

    @Serializable
    data object Bill : NavigationRoute()

    @Serializable
    data class Home(
        val agrmId: Long
    ) : NavigationRoute()

    @Serializable
    data class HistoryPaymentRoute(
        val agrmId: Long,
        val vgId: Long,
        val tariffId: Long,
    ) : NavigationRoute()

    @Serializable
    data object Blockings : NavigationRoute()

    @Serializable
    data object PasswordChanger : NavigationRoute()


}