package com.impuls.billing.data.repository

import com.impuls.billing.domain.model.Account
import com.impuls.billing.domain.model.Agreement
import com.impuls.billing.domain.model.ClientAccount
import com.impuls.billing.domain.utils.findInXmlString


fun formClientAccount(xml: String): ClientAccount {
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
}