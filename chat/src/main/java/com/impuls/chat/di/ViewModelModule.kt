package com.impuls.chat.di


import com.impuls.chat.ui.chat_screen.ChatScreenViewModel
import com.impuls.chat.ui.main_screen.MainScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelChatModule = module {
    viewModelOf(::MainScreenViewModel)
    viewModelOf(::ChatScreenViewModel)
}
