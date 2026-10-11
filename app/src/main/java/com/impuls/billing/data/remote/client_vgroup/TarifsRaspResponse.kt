package com.impuls.billing.data.remote.client_vgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TarifsRaspResponse(
    val force: Short,                       // Принудительно сменить тариф
    val recordId: Long,                     // Идентификатор записи
    val vgId: Long,                         // Идентификатор учётной записи
    val agrmId: Long,                       // Идентификатор договора
    val groupId: Long,                      // Идентификатор объединения (или 0)
    val uid: Long,                          // Идентификатор пользователя
    val id: Long,                           // Идентификатор агента
    val tarIdNew: Long,                     // Идентификатор нового тарифа
    val tarIdOld: Long,                     // Идентификатор старого тарифа
    val agentType: Long,                    // Тип агента
    val tarNewCurId: Long,                  // Идентификатор валюты для нового тарифа
    val tarOldCurId: Long,                  // Идентификатор валюты для старого тарифа
    val requestBy: String,                 // Идентификатор менеджера (или NULL)
    val changeTime: String,                 // Запланированное время смены тарифа (YYYY-MM-DD HH24:MI:SS)
    val timeTo: String,                     // Время окончания действия тарифа
    val vgLogin: String,                    // Логин учётной записи
    val agrmNum: String,                    // Номер договора
    val code: String,                       // Код оплаты
    val accName: String,                    // Имя пользователя (ФИО или название компании)
    val agentName: String,                  // Имя агента
    val tarNewName: String,                 // Название нового тарифного плана
    val tarOldName: String,                 // Название старого тарифного плана
    val mgrName: String,                    // ФИО менеджера или абонента
    val tarNewSymbol: String,               // Символ валюты нового тарифа
    val tarOldSymbol: String,               // Символ валюты старого тарифа
    val keepAllModifiers: Long,             // Правило переноса скидок: 0, 1, 2
    val keepTarModifier: Long,              // Правило переноса скидки: 0 или 1
    val discount: Double,                   // Коэффициент скидки
    val absDiscount: Double,                // Величина скидки на абонентскую плату
    val absBlockDiscount: Double,           // Величина скидки при блокировке
    val rent: Double,                       // Новая абонентская плата
    val blockRent: Double,                  // Новая абонентская плата при блокировке
    val servCatIdx: Long,                   // Идентификатор категории услуги
    val tarNewRentAsService: Boolean,       // Флаг "Абонентская плата как услуга"
    val catDiscounts: List<CategoryDiscountResponse> // Список скидок для категорий тарифа
)
