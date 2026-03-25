package com.impuls.billing.data.mapper

import com.impuls.billing.data.remote.client_account.GetClientAccountResponse
import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.utils.convertBillDeliveryType
import com.impuls.billing.domain.utils.convertPaymentMethodTYpe
import com.impuls.billing.domain.utils.convertToBoolean
import com.impuls.billing.domain.utils.convertUserType


/*fun formClientAccount(xml: String): ClientAccount {
    return ClientAccount(
        billDeliveryName = findInXmlString(
            tag = "billdeliveryname",
            xml = xml
        )?:"",
        account = Account(
            uid = findInXmlString(
                tag = "uid",
                xml = xml
            )?.toIntOrNull()?:0,
            doctype = findInXmlString(
                tag = "doctype",
                xml = xml
            )?.toIntOrNull()?:0,
            billDelivery = findInXmlString(
                tag = "billdelivery",
                xml = xml
            )?.toIntOrNull()?:0,
            category = findInXmlString(
                tag = "category",
                xml = xml
            )?.toIntOrNull()?:0,
            type = findInXmlString(
                tag = "type",
                xml = xml
            )?.toIntOrNull()?:0,
            oksm = findInXmlString(
                tag = "oksm",
                xml = xml
            )?.toIntOrNull()?:0,
            wrongActive = findInXmlString(
                tag = "wrongactive",
                xml = xml
            )?.toIntOrNull()?:0,
            archive = (findInXmlString(
                tag = "archive",
                xml = xml
            )?.toIntOrNull() ?: 0) != 0,
            ownerShip = findInXmlString(
                tag = "ownership",
                xml = xml
            )?.toIntOrNull()?:0,
            mobileIsConfirmed = findInXmlString(
                tag = "mobileisconfirmed",
                xml = xml
            )?.toBoolean()?:false,
            emailIsConfirmed = findInXmlString(
                tag = "emailisconfirmed",
                xml = xml
            )?.toBoolean()?:false,
            offerIsAccepted = findInXmlString(
                tag = "offerisaccepted",
                xml = xml
            )?.toBoolean()?:false,
            soleProprietor = findInXmlString(
                tag = "soleproprietor",
                xml = xml
            )?.toBoolean()?:false,
            login = findInXmlString(
                tag = "login",
                xml = xml
            )?:"",
            password = findInXmlString(
                tag = "pass",
                xml = xml
            )?:"",
            description = findInXmlString(
                tag = "descr",
                xml = xml
            )?:"",
            name = findInXmlString(
                tag = "name",
                xml = xml
            )?:"",
            phone = findInXmlString(
                tag = "phone",
                xml = xml
            )?:"",
            fax = findInXmlString(
                tag = "fax",
                xml = xml
            )?:"",
            email = findInXmlString(
                tag = "email",
                xml = xml
            )?:"",
            mobile = findInXmlString(
                tag = "mobile",
                xml = xml
            )?:"",
            bankName =findInXmlString(
                tag = "bankname",
                xml = xml
            )?:"",
            branchBankName = findInXmlString(
                tag = "branchbankname",
                xml = xml
            )?:"",
            treasuryName = findInXmlString(
                tag = "treasuryname",
                xml = xml
            )?:"",
            treasuryAccount = findInXmlString(
                tag = "treasuryaccount",
                xml = xml
            )?:"",
            bik = findInXmlString(
                tag = "bik>",
                xml = xml
            )?:"",
            setti = findInXmlString(
                tag = "settl",
                xml = xml
            )?:"",
            corr = findInXmlString(
                tag = "corr",
                xml = xml
            )?:"",
            kpp = findInXmlString(
                tag = "kpp",
                xml = xml
            )?:"",
            inn = findInXmlString(
                tag = "inn",
                xml = xml
            )?:"",
            ogrn = findInXmlString(
                tag = "ogrn",
                xml = xml
            )?:"",
            okpo = findInXmlString(
                tag = "okpo",
                xml = xml
            )?:"",
            okved = findInXmlString(
                tag = "okved",
                xml = xml
            )?:"",
            abonentName = findInXmlString(
                tag = "abonentname",
                xml = xml
            )?:"",
            abonentSurname = findInXmlString(
                tag = "abonentsurname",
                xml = xml
            )?:"",
            abonentPatronymic = findInXmlString(
                tag = "abonentpatronymic",
                xml = xml
            )?:""
        ),
        agreement = Agreement(
            agrmId = findInXmlString(
                tag = "agrmid",
                xml = xml
            )?.toIntOrNull()?:0,
            uid = findInXmlString(
                tag = "uid",
                xml = xml
            )?.toIntOrNull()?:0,
            operId = findInXmlString(
                tag = "operid",
                xml = xml
            )?.toIntOrNull()?:0,
            curId = findInXmlString(
                tag = "curid",
                xml = xml
            )?.toIntOrNull()?:0,
            bNotify = findInXmlString(
                tag = "bnotify",
                xml = xml
            )?.toIntOrNull()?:0,
            vGroups = findInXmlString(
                tag = "vgroups",
                xml = xml
            )?.toIntOrNull()?:0,
            penaltyMethod = findInXmlString(
                tag = "penaltymethod",
                xml = xml
            )?.toIntOrNull()?:0,
            monthBlockDay = findInXmlString(
                tag = "monthblockday",
                xml = xml
            )?.toIntOrNull()?:0,
            balance = findInXmlString(
                tag = "balance",
                xml = xml
            )?.toDoubleOrNull()?:0.0,
            credit = findInXmlString(
                tag = "credit",
                xml = xml
            )?.toDoubleOrNull()?:0.0,
            promiseCredit = findInXmlString(
                tag = "promisecredit",
                xml = xml
            )?.toDoubleOrNull()?:0.0,
            installments = findInXmlString(
                tag = "installments",
                xml = xml
            )?.toDoubleOrNull()?:0.0,
            balanceStatus = findInXmlString(
                tag = "balancestatus",
                xml = xml
            )?.toIntOrNull()?:0,
            friendAgrmId = findInXmlString(
                tag = "friendagrmid",
                xml = xml
            )?.toIntOrNull()?:0,
            paymentMethod = findInXmlString(
                tag = "paymentmethod",
                xml = xml
            )?.toIntOrNull()?:0,
            blockDays = findInXmlString(
                tag = "blockdays",
                xml = xml
            )?.toIntOrNull()?:0,
            blockMonths = findInXmlString(
                tag = "blockmonths",
                xml = xml
            )?.toIntOrNull()?:0,
            orderPayDay = findInXmlString(
                tag = "orderpayday",
                xml = xml
            )?.toIntOrNull()?:0,
            blockOrders = findInXmlString(
                tag = "blockorders",
                xml = xml
            )?.toIntOrNull()?:0,
            blockAmount = findInXmlString(
                tag = "blockamount",
                xml = xml
            )?.toDoubleOrNull()?:0.0,
            ownerId = findInXmlString(
                tag = "ownerid",
                xml = xml
            )?.toIntOrNull()?:0,
            isDefault = findInXmlString(
                tag = "isdefault",
                xml = xml
            )?.toIntOrNull()?:0,
            number = findInXmlString(
                tag = "number",
                xml = xml
            )?:"",
            date = findInXmlString(
                tag = "date",
                xml = xml
            )?:"",
            closeDon = findInXmlString(
                tag = "closedon",
                xml = xml
            )?:"",
            bCheck = findInXmlString(
                tag = "bcheck",
                xml = xml
            )?:"",
            symbol = findInXmlString(
                tag = "symbol",
                xml = xml
            )?:"",
            username = findInXmlString(
                tag = "username",
                xml = xml
            )?:"",
            initialBalance = findInXmlString(
                tag = "initialbalance",
                xml = xml
            )?.toDoubleOrNull()?:0.0,
        )
    )
}*/

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

