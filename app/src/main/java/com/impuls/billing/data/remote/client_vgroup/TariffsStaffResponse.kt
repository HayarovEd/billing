package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TariffsStaffResponse(
    /**
     * Идентификатор группы тарифов
     */
    val groupId: Long,

    /**
     * Идентификатор тарифного плана, вокруг которого собрана группа
     */
    val groupTarId: Long,

    /**
     * Идентификатор сетевого агента
     */
    val groupModuleId: Long,

    /**
     * Идентификатор тарифного плана, который является частью группы
     */
    val tarId: Long,

    /**
     * Идентификатор типа тарифного плана
     *
     * Возможные значения:
     * * 0 -- выделенная линия
     * * 1 -- DialUP по объему
     * * 2 -- DialUP по времени
     * * 3 -- телефония
     * * 4 -- IP-телефония
     * * 5 -- услуги (тариф UsBox-услуг)
     */
    val tarType: Long,

    /**
     * Идентификатор валюты, в которой задана абонентская плата тарифного плана
     */
    val tarCurId: Long,

    /**
     * Ограничение полосы пропускания для тарифных планов услуги "Интернет"
     */
    val shape: Long,

    /**
     * Величина абонентской платы по тарифному плану
     */
    val rent: Double,

    /**
     * Название тарифного плана, который является участником группы
     */
    val tarName: String,

    /**
     * Символ валюты, в которой задана абонентская плата тарифного плана (участника группы)
     */
    val tarSymbol: String,

    /**
     * Описание тарифного плана, который является участником группы
     */
    val tarDescrFull: String
)
