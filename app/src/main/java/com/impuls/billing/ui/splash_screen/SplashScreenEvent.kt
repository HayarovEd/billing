package com.impuls.billing.ui.splash_screen

sealed interface SplashScreenEvent {
    data object ProfileNavigateEvent : SplashScreenEvent
    data object LoginNavigateEvent : SplashScreenEvent
}