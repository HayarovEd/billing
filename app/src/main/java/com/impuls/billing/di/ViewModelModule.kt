package com.impuls.billing.di


import com.impuls.billing.ui.login_screen.LoginScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginScreenViewModel)
    /*viewModelOf(::PlayerScreenViewModel)
    viewModelOf(::ChannelsScreenViewModel)
    viewModelOf(::SplashScreenViewModel)*/
}
