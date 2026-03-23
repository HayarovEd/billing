package com.impuls.billing.ui.uikit.model

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object Login : NavigationRoute()

    @Serializable
    data object Splash : NavigationRoute()

    @Serializable
    data object Accounts : NavigationRoute()

    @Serializable
    data object Profile : NavigationRoute()

    @Serializable
    data object Payment : NavigationRoute()

    @Serializable
    data object Bill : NavigationRoute()

    @Serializable
    data object HomeInternet : NavigationRoute()

    @Serializable
    data object Blockings : NavigationRoute()

    @Serializable
    data object PasswordChanger : NavigationRoute()


}