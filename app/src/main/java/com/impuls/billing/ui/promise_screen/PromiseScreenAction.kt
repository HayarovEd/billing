package com.impuls.billing.ui.promise_screen

sealed interface PromiseScreenAction {
    class UpdateAmount(val amount: String): PromiseScreenAction
}