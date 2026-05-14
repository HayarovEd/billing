package com.impuls.billing.ui.profile_screen

import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.SharedPost
import com.impuls.billing.domain.model.VgroupUsbBox

data class ProfileScreenState(
    val account: ClientAccount? = null,
    val isLoading: Boolean = false,
    val count: Int = 0,
    val phoneRet: Long? = null,
    val emailRet: Long? = null,
    val vGroups: List<ClientVroup> = emptyList(),
    val vGroupUsbBoxes: List<VgroupUsbBox> = emptyList(),
    val vGroupPassword: String = "",
    val sharedPosts: List<SharedPost> = emptyList(),
) {
    val clientVGroups = vGroups.firstOrNull()
    val filteredSharedPosts = sharedPosts.filter { it.status == 1 }
}