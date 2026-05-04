package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.BlockVgroup
import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import kotlinx.datetime.LocalDate

interface BlockingRepository {
    suspend fun insertBlocking(
        token: String,
        vgId: Long,
        agrmId: Long,
        changeTime: LocalDate,
        blkReq: Int
    ): ResultWork<Long, DataError>

    suspend fun getBlocking(
        token: String,
        vgId: Long,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): ResultWork<List<BlockVgroup>, DataError>
}