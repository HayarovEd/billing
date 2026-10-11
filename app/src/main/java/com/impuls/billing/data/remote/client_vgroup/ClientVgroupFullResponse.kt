package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class ClientVgroupFullResponse(
    /**
     * Основная информация об учётной записи
     */
    val vGroup: ClientVgroupResponse,

    /**
     * Расписание смены тарифов
     */
    val tarRasp: List<TarifsRaspResponse>,

    /**
     * Сетевые сегменты
     */
    val staff: List<StaffResponse>,

    /**
     * Транспортные адреса
     */
    val macstaff: List<MacStaffResponse>,

    /**
     * Тарифы, доступные для перехода
     */
    val tarstaff: List<TariffsStaffResponse>,

    /**
     * История временного увеличения скорости
     */
    val turboshape: List<TurboShapeResponse>,

    /**
     * Дополнительные поля учётной записи
     */
    val addons: List<VgroupAddonResponse>,

    /**
     * История блокировок
     */
    val blockrasp: List<BlockRaspResponse>,

    /**
     * Адреса учётной записи
     */
    val addresses: List<AddressBriefResponse>
)
