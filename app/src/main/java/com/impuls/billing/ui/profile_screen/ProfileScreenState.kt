package com.impuls.billing.ui.profile_screen

import com.impuls.billing.domain.model.ClientAccount

data class ProfileScreenState(
    val account: ClientAccount? = null,
    val isLoading: Boolean = false,
    val count: Int = 0,
    val phoneRet: Long? = null,
    val emailRet: Long? = null,
)