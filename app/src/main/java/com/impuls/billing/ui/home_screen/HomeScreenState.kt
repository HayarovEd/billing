package com.impuls.billing.ui.home_screen

import com.impuls.billing.domain.model.ClientVroup

data class HomeScreenState(
    val agrmId: Long,
    val vGroups: List<ClientVroup> = emptyList(),
    val isLoading: Boolean = false,
    val selectedVGroupId: Long? = null,
)