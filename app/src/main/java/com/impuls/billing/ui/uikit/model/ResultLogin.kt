package com.impuls.billing.ui.uikit.model


sealed interface ResultLogin {
    data class Success(val uid: Long): ResultLogin
    data class Error (val message: String): ResultLogin
}