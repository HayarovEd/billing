package com.impuls.billing.data.mapper

import com.impuls.billing.data.remote.client_account.GetClientAccountResponse
import com.impuls.billing.data.remote.image_data.ImageDataResponse
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.model.payment.ImageData
import com.impuls.billing.domain.utils.convertBillDeliveryType
import com.impuls.billing.domain.utils.convertPaymentMethodTYpe
import com.impuls.billing.domain.utils.convertToBoolean
import com.impuls.billing.domain.utils.convertUserType


fun GetClientAccountResponse.toClientAccount(): ClientAccount {
    return ClientAccount(
        billDeliveryName = ret.billdeliveryname,
        account = Account(
            uid = ret.account.uid,
            doctype = ret.account.doctype,
            billDelivery = convertBillDeliveryType(ret.account.billdelivery),
            category = ret.account.category,
            type = convertUserType(ret.account.type),
            oksm = ret.account.oksm,
            wrongActive = ret.account.wrongactive,
            archive = convertToBoolean(ret.account.archive),
            ownerShip = ret.account.ownership,
            mobileIsConfirmed = ret.account.mobileisconfirmed,
            emailIsConfirmed = ret.account.emailisconfirmed,
            offerIsAccepted = ret.account.offerisaccepted,
            soleProprietor = ret.account.soleproprietor,
            login = ret.account.login,
            password = ret.account.pass,
            description = ret.account.descr,
            name = ret.account.name,
            phone = ret.account.phone,
            fax = ret.account.fax,
            email = ret.account.email,
            mobile = ret.account.mobile,
            bankName = ret.account.bankname,
            branchBankName = ret.account.branchbankname,
            treasuryName = ret.account.treasuryname,
            treasuryAccount = ret.account.treasuryaccount,
            bik = ret.account.bik,
            setti = ret.account.settl,
            corr = ret.account.corr,
            kpp = ret.account.kpp,
            inn = ret.account.inn,
            ogrn = ret.account.ogrn,
            okpo = ret.account.okpo,
            okved = ret.account.okved,
            abonentName = ret.account.abonentname,
            abonentSurname = ret.account.abonentsurname,
            abonentPatronymic = ret.account.abonentpatronymic
        ),
        agreements = ret.agreements.map { 
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

