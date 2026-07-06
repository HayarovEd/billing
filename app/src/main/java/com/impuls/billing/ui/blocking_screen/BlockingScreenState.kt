package com.impuls.billing.ui.blocking_screen

import com.impuls.billing.domain.model.BlockVgroup
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.PeriodHistory
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class BlockingScreenState(
    val vGroups: List<ClientVroup> = emptyList(),
    val isLoading: Boolean = false,
    val selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val historyBlocks: List<BlockVgroup> = emptyList(),
    val periodHistory: PeriodHistory = PeriodHistory.Today,
) {
    val vgroup = vGroups.firstOrNull()
}