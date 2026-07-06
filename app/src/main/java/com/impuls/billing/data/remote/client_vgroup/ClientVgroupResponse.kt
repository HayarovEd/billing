package com.impuls.billing.data.remote.client_vgroup
import kotlinx.serialization.Serializable

@Serializable
data class ClientVgroupResponse(
    val agrmId: Long, //		Идентификатор договора
    val vgId: Long, //		Идентификатор учётной записи
    val blkReq: Long, //		Запрос на блокировку
    val blocked: Long, //		Текущее состояние блокировки
    val changed: Long, //		Признак изменения учётной записи     0 — изменений нет 3 — новая учётная запись 4 — изменена 5 — удалена
    val agentId: Long, //		Идентификатор агента
    val tarifId: Long, //		Идентификатор тарифа
    val tarifType: Long, //		Тип тарифа
    val curShape: Long, //		Текущее значение полосы пропускания
    val usesmartCards: Long, //		Использовать смарткарты
    val useCas: Long, //		Использовать CAS
    val radiusInsertMacStaff: Long, //		Сохранять MAC-адрес из запросов авторизации
    val multiply: Long, //		Значение multiply из настроек агента
    val serviceRent: Double, //		Величина аренды в валюте тарифа
    val serviceVolume: Long, //		Сумма стоимостей услуг, включённая в абонентскую плату
    val serviceUsedIn: Long, //		Входящий трафик
    val serviceUsedOut: Long, //		Исходящий трафики
    val agentDescr: String, //		Описание агента
    val tarifDescr: String, //		Описание тарифа
    val login: String, //		Логин учётной записи
    val currentModifier: CurrentModifierResponse, //		Текущая скидка
)
