package com.impuls.billing.di


import com.impuls.billing.ui.blocking_screen.BlockingScreenViewModel
import com.impuls.billing.ui.home_screen.HomeScreenViewModel
import com.impuls.billing.ui.login_screen.LoginScreenViewModel
import com.impuls.billing.ui.payment_history.HistoryPaymentScreenViewModel
import com.impuls.billing.ui.payment_screen.PaymentScreenViewModel
import com.impuls.billing.ui.profile_screen.ProfileScreenViewModel
import com.impuls.billing.ui.promise_screen.PromiseScreenViewModel
import com.impuls.billing.ui.settings_screen.SettingsScreenViewModel
import com.impuls.billing.ui.splash_screen.SplashScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::ProfileScreenViewModel)
    viewModelOf(::PaymentScreenViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::HistoryPaymentScreenViewModel)
    viewModelOf(::SettingsScreenViewModel)
    viewModelOf(::BlockingScreenViewModel)
    viewModelOf(::PromiseScreenViewModel)
}
