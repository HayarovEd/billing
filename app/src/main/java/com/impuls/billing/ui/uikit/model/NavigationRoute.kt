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
    data class PaymentRpute(
        val number: String
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


}