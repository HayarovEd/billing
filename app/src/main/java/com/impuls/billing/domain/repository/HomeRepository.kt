package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.VgroupUsbBox
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork

interface HomeRepository {
    suspend fun getShortStatistics(
        token: String,
        agrmId: Long?,
        vgId: Long?
    ): ResultWork<List<ClientVroup>, DataError>

    suspend fun getUsbBoxByVgroup(
        token: String,
        vgId: Long
    ): ResultWork<List<VgroupUsbBox>, DataError>
}