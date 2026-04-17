package com.impuls.billing.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.impuls.billing.ui.home_screen.HomeScreenRoot
import com.impuls.billing.ui.login_screen.LoginScreenRoot
import com.impuls.billing.ui.payment_screen.PaymentScreenRoot
import com.impuls.billing.ui.profile_screen.ProfileScreenRoot
import com.impuls.billing.ui.splash_screen.SplashScreenRoot
import com.impuls.billing.ui.uikit.PortraitOnly
import com.impuls.billing.ui.uikit.model.NavigationRoute


@Composable
fun NavController(
    startDestination: NavigationRoute = NavigationRoute.Splash/*Payment("595142026")*/,
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

        composable<NavigationRoute.Splash> {
            SplashScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.Login)
                },
                onNavigateToProfile = {
                    navController.navigate(NavigationRoute.Profile)
                }
            )
        }
        composable<NavigationRoute.Login> {
            LoginScreenRoot(
                onNavigateToAccounts = {
                    navController.navigate(NavigationRoute.Profile)
                }
            )
        }
        composable<NavigationRoute.Profile> {
            ProfileScreenRoot(
                onNavigateToPayment = {
                    navController.navigate(NavigationRoute.Payment(it))
                },
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.Login)
                   // navController.popBackStack(NavigationRoute.Login, false)
                },
                onNavigateToMore = {
                    navController.navigate(NavigationRoute.Home(it))
                }
            )
        }

        composable<NavigationRoute.Payment> {
            PaymentScreenRoot(
                navigateToBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<NavigationRoute.Home> {
            HomeScreenRoot (
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.Login)
                    // navController.popBackStack(NavigationRoute.Login, false)
                },
                onNavigateToBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}