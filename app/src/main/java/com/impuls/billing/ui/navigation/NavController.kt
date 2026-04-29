package com.impuls.billing.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.impuls.billing.ui.home_screen.HomeScreenRoot
import com.impuls.billing.ui.login_screen.LoginScreenRoot
import com.impuls.billing.ui.payment_history.HistoryPaymentScreenRoot
import com.impuls.billing.ui.payment_screen.PaymentScreenRoot
import com.impuls.billing.ui.profile_screen.ProfileScreenRoot
import com.impuls.billing.ui.settings_screen.SettingsScreenRoot
import com.impuls.billing.ui.splash_screen.SplashScreenRoot
import com.impuls.billing.ui.uikit.PortraitOnly
import com.impuls.billing.ui.uikit.model.NavigationRoute


@Composable
fun NavController(
    startDestination: NavigationRoute = NavigationRoute.SplashRoute,/*Payment("595142026")*/
) {
    val navController = rememberNavController()
    PortraitOnly()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {

        composable<NavigationRoute.SplashRoute> {
            SplashScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute)
                },
                onNavigateToProfile = {
                    navController.navigate(NavigationRoute.ProfileRoute)
                }
            )
        }
        composable<NavigationRoute.LoginRoute> {
            LoginScreenRoot(
                onNavigateToAccounts = {
                    navController.navigate(NavigationRoute.ProfileRoute)
                }
            )
        }
        composable<NavigationRoute.ProfileRoute> {
            ProfileScreenRoot(
                onNavigateToPayment = {
                    navController.navigate(NavigationRoute.PaymentRpute(it))
                },
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute)
                    // navController.popBackStack(NavigationRoute.Login, false)
                },
                onNavigateToMore = {
                    navController.navigate(NavigationRoute.HomeRoute(it))
                },
                onClickPaymentHistory = { agrmId, vgId, tarifId ->
                    navController.navigate(
                        NavigationRoute.HistoryPaymentRoute(
                            agrmId = agrmId,
                            vgId = vgId,
                            tariffId = tarifId
                        )
                    )
                },
                onNavigateToSettings = {
                    navController.navigate(NavigationRoute.SettingsRoute(it))
                }
            )
        }

        composable<NavigationRoute.PaymentRpute> {
            PaymentScreenRoot(
                navigateToBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<NavigationRoute.HomeRoute> {
            HomeScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute)
                    // navController.popBackStack(NavigationRoute.Login, false)
                },
                onNavigateToBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<NavigationRoute.HistoryPaymentRoute> {
            HistoryPaymentScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute)
                }
            )
        }

        composable<NavigationRoute.SettingsRoute> {
            SettingsScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute) {
                        popUpTo<NavigationRoute.LoginRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}