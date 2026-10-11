package com.impuls.billing.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.impuls.billing.ui.blocking_screen.BlockingScreenRoot
import com.impuls.billing.ui.home_screen.HomeScreenRoot
import com.impuls.billing.ui.login_screen.LoginScreenRoot
import com.impuls.billing.ui.payment_history.HistoryPaymentScreenRoot
import com.impuls.billing.ui.payment_screen.PaymentScreenRoot
import com.impuls.billing.ui.profile_screen.ProfileScreenRoot
import com.impuls.billing.ui.promise_screen.PromiseScreenRoot
import com.impuls.billing.ui.settings_screen.SettingsScreenRoot
import com.impuls.billing.ui.splash_screen.SplashScreenRoot
import com.impuls.billing.ui.uikit.PortraitOnly
import com.impuls.billing.ui.uikit.model.NavigationRoute
import com.impuls.chat.ui.chat_screen.ChatScreenRoot
import com.impuls.chat.ui.main_screen.MainScreenRoot
import com.impuls.chat.ui.navigation.NavigationChatRoute


@Composable
fun NavController(
    navController: NavHostController = rememberNavController(),
    startDestination: NavigationRoute = NavigationRoute.SplashRoute,
) {
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
        composable<NavigationRoute.ProfileRoute>(
            deepLinks = listOf(
                navDeepLink<NavigationRoute.ProfileRoute>(basePath = "billing://profile/")
            )
        ) {
            ProfileScreenRoot(
                onNavigateToPayment = { number, operId ->
                    navController.navigate(
                        NavigationRoute.PaymentRoute(
                            number = number,
                            operId = operId
                        )
                    )
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
                onNavigateToSettings = { uid ->
                    navController.navigate(
                        NavigationRoute.SettingsRoute(
                            userId = uid,
                        )
                    )
                },
                onNavigateToBlocking = {
                    navController.navigate(
                        NavigationRoute.BlockingRoute(
                            vgId = it
                        )
                    )
                },
                onClickPromisePayment = {
                    navController.navigate(
                        NavigationRoute.PromiseRoute(
                            agrmId = it
                        )
                    )
                },
                onNavigateToMainChat = { name, login, email, groupName ->
                    navController.navigate(
                        NavigationChatRoute.MainRoute(
                            name = name,
                            login = login,
                            email = email,
                            operId = groupName,
                        )
                    )
                },
                onNavigateToChat = { chatId ->
                    navController.navigate(
                        NavigationChatRoute.ChatRoute(
                            chatId = chatId,
                        )
                    )
                }
            )
        }

        composable<NavigationRoute.PaymentRoute> {
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
                },
                onBack = {
                    navController.navigateUp()
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
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<NavigationRoute.BlockingRoute> {
            BlockingScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute) {
                        popUpTo<NavigationRoute.LoginRoute> {
                            inclusive = true
                        }
                    }
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<NavigationRoute.PromiseRoute> {
            PromiseScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.LoginRoute)
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        //chat
        composable<NavigationChatRoute.MainRoute> {
            MainScreenRoot(
                onNavigateToChat = { chatId->
                    navController.navigate(
                        NavigationChatRoute.ChatRoute(
                            chatId = chatId,
                        )
                    )
                }
            )
        }
        composable<NavigationChatRoute.ChatRoute> {
            ChatScreenRoot(
                onNavigateToLogin = {
                    navController.popBackStack<NavigationChatRoute.MainRoute>(inclusive = true)
                }
            )
        }
    }
}