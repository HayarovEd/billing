package com.impuls.billing.ui.login_screen

import com.impuls.billing.R

enum class RecoverType(val number: Int, val descriptionRes: Int) {
    EMAIL(number = 0, descriptionRes = R.string.email),
    SMS(number = 1, descriptionRes = R.string.mobile_phone)
}