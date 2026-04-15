package com.impuls.billing.di


import com.impuls.billing.ui.home_screen.HomeScreenViewModel
import com.impuls.billing.ui.login_screen.LoginScreenViewModel
import com.impuls.billing.ui.payment_screen.PaymentScreenViewModel
import com.impuls.billing.ui.profile_screen.ProfileScreenViewModel
import com.impuls.billing.ui.splash_screen.SplashScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
    viewModelOf(::PaymentScreenViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::HomeScreenViewModel)
}
