package com.impuls.billing.data.mapper

import com.impuls.billing.data.remote.client_account.old.AccountDto
import com.impuls.billing.data.remote.client_account.old.AgreementDto
import com.impuls.billing.data.remote.client_account.old.ClientAccountDataDto
import com.impuls.billing.data.remote.client_account.old.GetClientAccountResponse
import com.impuls.billing.data.remote.client_account.old.UserGroupDto
import com.impuls.billing.data.remote.client_account.old.UserGroupsDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import kotlin.text.toIntOrNull

class SoapParser {

    fun parseFullSoapResponse(xmlString: String): GetClientAccountResponse {
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())
        val retElement = doc.select("ret").first()!!
        return GetClientAccountResponse(parseRet(retElement))
    }

    private fun parseRet(retElement: Element): ClientAccountDataDto {
        return ClientAccountDataDto(
            application = retElement.select("application").text().toIntOrNull()?:0,
            billdeliveryname = retElement.select("billdeliveryname").text(),
            account = parseAccount(retElement.select("account").first()!!),
            usergroups = parseUserGroups(retElement.select("usergroups").first()!!),
            agreements = parseAgreements(retElement.select("agreements"))
        )
    }

    private fun parseAccount(accountElement: Element): AccountDto {
        return AccountDto(
            uid = accountElement.select("uid").text().toIntOrNull()?:0,
            doctype = accountElement.select("doctype").text().toIntOrNull()?:0,
            ipaccess = accountElement.select("ipaccess").text().toIntOrNull()?:0,
            billdelivery = accountElement.select("billdelivery").text().toIntOrNull()?:0,
            category = accountElement.select("category").text().toIntOrNull()?:0,
            type = accountElement.select("type").text().toIntOrNull()?:0,
            oksm = accountElement.select("oksm").text().toIntOrNull()?:0,
            templ = accountElement.select("templ").text().toIntOrNull()?:0,
            wrongactive = accountElement.select("wrongactive").text().toIntOrNull()?:0,
            archive = accountElement.select("archive").text().toIntOrNull()?:0,
            ownership = accountElement.select("ownership").text().toIntOrNull()?:0,
            mobileisconfirmed = accountElement.select("mobileisconfirmed").text().toBooleanStrictOrNull()?:false,
            emailisconfirmed = accountElement.select("emailisconfirmed").text().toBooleanStrictOrNull()?:false,
            offerisaccepted = accountElement.select("offerisaccepted").text().toBooleanStrictOrNull()?:false,
            soleproprietor = accountElement.select("soleproprietor").text().toBooleanStrictOrNull()?:false,
            login = accountElement.select("login").text(),
            pass = accountElement.select("pass").text(),
            descr = accountElement.select("descr").text(),
            name = accountElement.select("name").text(),
            phone = accountElement.select("phone").text(),
            fax = accountElement.select("fax").text(),
            email = accountElement.select("email").text(),
            mobile = accountElement.select("mobile").text(),
            bankname = accountElement.select("bankname").text(),
            branchbankname = accountElement.select("branchbankname").text(),
            treasuryname = accountElement.select("treasuryname").text(),
            treasuryaccount = accountElement.select("treasuryaccount").text(),
            bik = accountElement.select("bik").text(),
            settl = accountElement.select("settl").text(),
            corr = accountElement.select("corr").text(),
            kpp = accountElement.select("kpp").text(),
            inn = accountElement.select("inn").text(),
            ogrn = accountElement.select("ogrn").text(),
            okpo = accountElement.select("okpo").text(),
            okved = accountElement.select("okved").text(),
            gendiru = accountElement.select("gendiru").text(),
            glbuhgu = accountElement.select("glbuhgu").text(),
            kontperson = accountElement.select("kontperson").text(),
            actonwhat = accountElement.select("actonwhat").text(),
            passsernum = accountElement.select("passsernum").text(),
            passno = accountElement.select("passno").text(),
            passissuedate = accountElement.select("passissuedate").text(),
            passissuedep = accountElement.select("passissuedep").text(),
            passissueplace = accountElement.select("passissueplace").text(),
            birthdate = accountElement.select("birthdate").text(),
            birthplace = accountElement.select("birthplace").text(),
            lastmoddate = accountElement.select("lastmoddate").text(),
            wrongdate = accountElement.select("wrongdate").text(),
            okato = accountElement.select("okato").text(),
            uuid = accountElement.select("uuid").text(),
            abonentname = accountElement.select("abonentname").text(),
            abonentsurname = accountElement.select("abonentsurname").text(),
            abonentpatronymic = accountElement.select("abonentpatronymic").text(),
            managerid = accountElement.select("managerid").text().toIntOrNull()?:-1,
            managername = accountElement.select("managername").text(),
            managerlogin = accountElement.select("managerlogin").text(),
            swift = accountElement.select("swift").text(),
            kio = accountElement.select("kio").text(),
            bicbei = accountElement.select("bicbei").text(),
            iban = accountElement.select("iban").text(),
            bankcorr = accountElement.select("bankcorr").text(),
            bankcorrcode = accountElement.select("bankcorrcode").text(),
            bankcorraccount = accountElement.select("bankcorraccount").text(),
            currency = accountElement.select("currency").text(),
            resident = accountElement.select("resident").text().toIntOrNull()?:0,
            vatonfiscalization = accountElement.select("vatonfiscalization").text().toIntOrNull()?:0,
        )
    }

    private fun parseUserGroups(usergroupsElement: Element): UserGroupsDto {
        return UserGroupsDto(
            usergroups = parseUserGroup(usergroupsElement.select("usergroup")),
            usercnt = usergroupsElement.select("usercnt").text().toIntOrNull()?:0,
            fread = usergroupsElement.select("fread").text().toIntOrNull()?:0,
            fwrite = usergroupsElement.select("fwrite").text().toIntOrNull()?:0,
            defaultgroup = usergroupsElement.select("defaultgroup").text().toIntOrNull()?:0,
        )
    }

    private fun parseUserGroup(userGroupsElement: List<Element>): List<UserGroupDto> {
        return userGroupsElement.map { userGroupsElement->
            UserGroupDto(
                unloadtosorm = userGroupsElement.select("unloadtosorm").text().toIntOrNull()?:-1,
                sormid = userGroupsElement.select("sormid").text().toIntOrNull()?:0,
                groupid = userGroupsElement.select("groupid").text().toIntOrNull()?:0,
                promiseallow = userGroupsElement.select("promiseallow").text().toIntOrNull()?:0,
                promiseallowmanager = userGroupsElement.select("promiseallowmanager").text().toIntOrNull()?:0,
                promiserent = userGroupsElement.select("promiserent").text().toIntOrNull()?:0,
                promisetill = userGroupsElement.select("promisetill").text().toIntOrNull()?:0,
                promiseondays = userGroupsElement.select("promiseondays").text().toIntOrNull()?:0,
                promiseblockdays = userGroupsElement.select("promiseblockdays").text().toIntOrNull()?:0,
                promisemax = userGroupsElement.select("promisemax").text().toIntOrNull()?:0,
                promisemin = userGroupsElement.select("promisemin").text().toIntOrNull()?:0,
                promiselimit = userGroupsElement.select("promiselimit").text().toIntOrNull()?:0,
                name = userGroupsElement.select("name").text(),
                description = userGroupsElement.select("description").text(),
                vendorsormname = userGroupsElement.select("vendorsormname").text(),
                uuid = userGroupsElement.select("uuid").text(),
                uniquegroup = userGroupsElement.select("uniquegroup").text().toIntOrNull()?:-1,
            )
        }
    }

    private fun parseAgreements(agreementsElements: Elements): List<AgreementDto> {
        val agreements = mutableListOf<AgreementDto>()

        for (agreementElement in agreementsElements) {
            agreements.add(
                AgreementDto(
                    agrmid = agreementElement.select("agrmid").text().toIntOrNull()?:0,
                    uid = agreementElement.select("uid").text().toIntOrNull()?:0,
                    operid = agreementElement.select("operid").text().toIntOrNull()?:0,
                    curid = agreementElement.select("curid").text().toIntOrNull()?:0,
                    bnotify = agreementElement.select("bnotify").text().toIntOrNull()?:0,
                    archive = agreementElement.select("archive").text().toIntOrNull()?:0,
                    vgroups = agreementElement.select("vgroups").text().toIntOrNull()?:0,
                    penaltymethod = agreementElement.select("penaltymethod").text().toIntOrNull()?:0,
                    monthblockday = agreementElement.select("monthblockday").text().toIntOrNull()?:0,
                    agrmtype = agreementElement.select("agrmtype").text().toIntOrNull()?:0,
                    balance = agreementElement.select("balance").text().toDoubleOrNull()?:0.0,
                    balanceacc = agreementElement.select("balanceacc").text().toDoubleOrNull()?:0.0,
                    credit = agreementElement.select("credit").text().toDoubleOrNull()?:0.0,
                    promisecredit = agreementElement.select("promisecredit").text().toDoubleOrNull()?:0.0,
                    installments = agreementElement.select("installments").text().toDoubleOrNull()?:0.0,
                    balancestrictlimit = agreementElement.select("balancestrictlimit").text().toIntOrNull()?:0,
                    blimit = agreementElement.select("blimit").text().toDoubleOrNull()?:0.0,
                    balancestatus = agreementElement.select("balancestatus").text().toIntOrNull()?:0,
                    isauto = agreementElement.select("isauto").text().toIntOrNull()?:0,
                    friendagrmid = agreementElement.select("friendagrmid").text().toIntOrNull()?:0,
                    parentagrmid = agreementElement.select("parentagrmid").text().toIntOrNull()?:0,
                    paymentmethod = agreementElement.select("paymentmethod").text().toIntOrNull()?:0,
                    blockdays = agreementElement.select("blockdays").text().toIntOrNull()?:0,
                    blockmonths = agreementElement.select("blockmonths").text().toIntOrNull()?:0,
                    orderpayday = agreementElement.select("orderpayday").text().toIntOrNull()?:0,
                    blockorders = agreementElement.select("blockorders").text().toIntOrNull()?:0,
                    blockamount = agreementElement.select("blockamount").text().toDoubleOrNull()?:0.0,
                    priority = agreementElement.select("priority").text().toIntOrNull()?:0,
                    ownerid = agreementElement.select("ownerid").text().toIntOrNull()?:0,
                    isdefault = agreementElement.select("isdefault").text().toIntOrNull()?:0,
                    nofinblock = agreementElement.select("nofinblock").text().toIntOrNull()?:-1,
                    friendnumber = agreementElement.select("friendnumber").text(),
                    parentnumber = agreementElement.select("parentnumber").text(),
                    balancelimitexceeded = agreementElement.select("balancelimitexceeded").text(),
                    number = agreementElement.select("number").text(),
                    code = agreementElement.select("code").text(),
                    date = agreementElement.select("date").text(),
                    closedon = agreementElement.select("closedon").text(),
                    datevalidto = agreementElement.select("datevalidto").text(),
                    bcheck = agreementElement.select("bcheck").text(),
                    symbol = agreementElement.select("symbol").text(),
                    username = agreementElement.select("username").text(),
                    opername = agreementElement.select("opername").text(),
                    agreementidbopos = agreementElement.select("agreementidbopos").text(),
                    descr = agreementElement.select("descr").text(),
                    balancetext = agreementElement.select("balancetext").text(),
                    initialbalance = agreementElement.select("initialbalance").text().toDoubleOrNull()?:0.0,
                    errormessage = agreementElement.select("errormessage").text()
                )
            )
        }

        return agreements
    }
}