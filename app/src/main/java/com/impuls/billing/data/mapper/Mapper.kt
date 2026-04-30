package com.impuls.billing.data.mapper

import com.impuls.billing.data.remote.ClientInfoDto
import com.impuls.billing.data.remote.ClientShortStatDto
import com.impuls.billing.data.remote.DebitDto
import com.impuls.billing.data.remote.RecoverPasswordDto
import com.impuls.billing.data.remote.client_account.ClientAccountResponse
import com.impuls.billing.data.remote.client_vgroup.ClientVgroupFullResponse
import com.impuls.billing.data.remote.connected_usb_box.VgroupUsbBoxDto
import com.impuls.billing.data.remote.image_data.ImageDataResponse
import com.impuls.billing.data.remote.notices.ClientNoticesDto
import com.impuls.billing.data.remote.payment_history.PaymentHistoryDto
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.ClientInfo
import com.impuls.billing.domain.model.ClientNotices
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.HistoryDebit
import com.impuls.billing.domain.model.HistoryPayment
import com.impuls.billing.domain.model.ModifierData
import com.impuls.billing.domain.model.Notice
import com.impuls.billing.domain.model.RecoverPassword
import com.impuls.billing.domain.model.ShortStat
import com.impuls.billing.domain.model.VgroupUsbBox
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.utils.fromBillingToLocalDateTime
import com.impuls.billing.domain.utils.convertBillDeliveryType
import com.impuls.billing.domain.utils.convertPaymentMethodTYpe
import com.impuls.billing.domain.utils.convertToBoolean
import com.impuls.billing.domain.utils.convertToHistoryStatusPayment
import com.impuls.billing.domain.utils.convertToTypeBlockVGroup
import com.impuls.billing.domain.utils.convertUserType
import com.impuls.billing.domain.utils.roundTo
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant


fun ClientAccountResponse.toClientAccount(): ClientAccount {
    return ClientAccount(
        billDeliveryName = billDeliveryName,
        account = Account(
            uid = account.uid,
            doctype = account.doctype,
            billDelivery = convertBillDeliveryType(account.billdelivery),
            category = account.category,
            type = convertUserType(account.type),
            oksm = account.oksm,
            wrongActive = account.wrongactive,
            archive = convertToBoolean(account.archive),
            ownerShip = account.ownership,
            mobileIsConfirmed = account.mobileisconfirmed,
            emailIsConfirmed = account.emailisconfirmed,
            offerIsAccepted = account.offerisaccepted,
            soleProprietor = account.soleproprietor,
            login = account.login,
            password = account.pass,
            description = account.descr,
            name = account.name,
            phone = account.phone,
            fax = account.fax,
            email = account.email,
            mobile = account.mobile,
            bankName = account.bankname,
            branchBankName = account.branchbankname,
            treasuryName = account.treasuryname,
            treasuryAccount = account.treasuryaccount,
            bik = account.bik,
            setti = account.settl,
            corr = account.corr,
            kpp = account.kpp,
            inn = account.inn,
            ogrn = account.ogrn,
            okpo = account.okpo,
            okved = account.okved,
            abonentName = account.abonentname,
            abonentSurname = account.abonentsurname,
            abonentPatronymic = account.abonentpatronymic
        ),
        agreements = agreements.map {
            Agreement(
                agrmId = it.agrmid,
                uid = it.uid,
                operId = it.operid,
                curId = it.curid,
                bNotify = it.bnotify,
                vGroups = it.vgroups,
                penaltyMethod = it.penaltymethod,
                monthBlockDay = it.monthblockday,
                balance = it.balance,
                credit = it.credit,
                promiseCredit = it.promisecredit,
                installments = it.installments,
                balanceStatus = it.balancestatus,
                friendAgrmId = it.friendagrmid,
                paymentMethod = convertPaymentMethodTYpe(it.paymentmethod),
                blockDays = it.blockdays,
                blockMonths = it.blockmonths,
                orderPayDay = it.orderpayday,
                blockOrders = it.blockorders,
                blockAmount = it.blockamount,
                ownerId = it.ownerid,
                isDefault = it.isdefault,
                number = it.number,
                date = it.date,
                closeDon = it.closedon,
                bCheck = it.bcheck,
                symbol = it.symbol,
                username = it.username,
                initialBalance = it.initialbalance,
                code = it.code
            )
        }
    )
}

fun ImageDataResponse.convertToImageData(): ImageData {
    return ImageData(
        color = result.color,
        image = result.image
    )
}

fun RecoverPasswordDto.convertToRecoverPassword(): RecoverPassword {
    return RecoverPassword(
        recordId = recordId,
        destination = destination
    )
}

fun ClientVgroupFullResponse.toClientVGroup(): ClientVroup {
    return ClientVroup(
        agrmId = vGroup.agrmId,
        vgId = vGroup.vgId,
        blocked = convertToTypeBlockVGroup(vGroup.blocked),
        tarifId = vGroup.tarifId,
        tarifDescr = vGroup.tarifDescr,
        login = vGroup.login,
        serviceVolume = vGroup.serviceVolume,
        serviceRent = vGroup.serviceRent,
        agentDescr = vGroup.agentDescr,
        speed = vGroup.curShape / 1000,
        currentModifier = ModifierData(
            type = vGroup.currentModifier.type,
            value = vGroup.currentModifier.value
        ),
        addresses = addresses.map { it.address }
    )
}

fun ClientShortStatDto.toShortStat(): ShortStat {
    return ShortStat(
        agrmId = agrmId ?: 0L,
        vgId = vgId ?: 0L,
        amount = amount ?: 0.0,
        agentDescr = agentDescr ?: "",
        currSymbol = currSymbol ?: ""
    )
}

fun PaymentHistoryDto.toHistoryPayment(): HistoryPayment {
    return HistoryPayment(
        timestamp = Instant.fromEpochSeconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        amount = amountCurr,
        uid = uid,
        status = convertToHistoryStatusPayment(payDto.status),
        currency = currSymb,
        message = mgr
    )
}


fun DebitDto.toHistoryDebit(): HistoryDebit {
    return HistoryDebit(
        amount = amount.roundTo(2),
        yearMonth = LocalDate.parse(yearMonth),
    )
}


fun VgroupUsbBoxDto.toVgroupUsbBox(): VgroupUsbBox {
    return VgroupUsbBox(
        activated = fromBillingToLocalDateTime(service.activated),
        state = service.state,
        rate = service.rate,
        price = catAbove,
        vgLogin = vgLogin,
        tarDescr = tarDescr,
        symbol = symbol
    )
}

fun ClientInfoDto.toClientInfo(): ClientInfo {
    return ClientInfo(
        email = email,
        emailIsConfirmed = emailIsConfirmed,
        kontPerson = kontPerson,
        mobile = mobile,
        mobileIsConfirmed = mobileIsConfirmed,
        offerIsAccepted = offerIsAccepted,
        passIsTemporary = passIsTemporary,
        password = password,
        phone = phone,
        triggerContactDataReminder = triggerContactDataReminder
    )
}

fun ClientNoticesDto.toClientNotices(): ClientNotices {
    return ClientNotices(
        uid = uid,
        notices = notices.map {
            Notice(
                descr = it.descr,
                email = it.email,
                messenger = it.messenger,
                name = it.name,
                sms = it.sms,
                type = it.type,
                value = it.value
            )
        }
    )
}


