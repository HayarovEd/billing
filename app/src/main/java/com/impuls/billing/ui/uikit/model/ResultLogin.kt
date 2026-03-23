package com.impuls.billing.ui.uikit.model

import com.impuls.billing.domain.utils.DataError

sealed interface ResultLogin {
    data class Success(val uid: Long): ResultLogin
    data class Error (val dataError: DataError): ResultLogin
}