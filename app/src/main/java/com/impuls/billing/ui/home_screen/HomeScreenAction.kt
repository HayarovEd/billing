package com.impuls.billing.ui.home_screen

sealed interface HomeScreenAction {
    class SelectVgroup(val vgId: Long): HomeScreenAction
}