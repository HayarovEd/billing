package com.impuls.billing.ui.blocking_screen

import kotlinx.datetime.LocalDate

sealed interface BlockingScreenAction {
    class UpdateSelectedDate(val date: LocalDate): BlockingScreenAction
}