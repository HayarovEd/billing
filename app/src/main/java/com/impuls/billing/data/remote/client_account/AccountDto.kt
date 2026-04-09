package com.impuls.billing.data.remote.client_account


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    @SerialName("abonentname")
    val abonentname: String,
    @SerialName("abonentpatronymic")
    val abonentpatronymic: String,
    @SerialName("abonentsurname")
    val abonentsurname: String,
    @SerialName("archive")
    val archive: Int,
    @SerialName("bankname")
    val bankname: String,
    @SerialName("bik")
    val bik: String,
    @SerialName("billdelivery")
    val billdelivery: Int,
    @SerialName("branchbankname")
    val branchbankname: String,
    @SerialName("category")
    val category: Int,
    @SerialName("corr")
    val corr: String,
    @SerialName("descr")
    val descr: String,
    @SerialName("doctype")
    val doctype: Int,
    @SerialName("email")
    val email: String,
    @SerialName("emailisconfirmed")
    val emailisconfirmed: Boolean,
    @SerialName("fax")
    val fax: String,
    @SerialName("inn")
    val inn: String,
    @SerialName("kpp")
    val kpp: String,
    @SerialName("login")
    val login: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("mobileisconfirmed")
    val mobileisconfirmed: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("offerisaccepted")
    val offerisaccepted: Boolean,
    @SerialName("ogrn")
    val ogrn: String,
    @SerialName("okpo")
    val okpo: String,
    @SerialName("oksm")
    val oksm: Int,
    @SerialName("okved")
    val okved: String,
    @SerialName("ownership")
    val ownership: Int,
    @SerialName("pass")
    val pass: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("settl")
    val settl: String,
    @SerialName("soleproprietor")
    val soleproprietor: Boolean,
    @SerialName("treasuryaccount")
    val treasuryaccount: String,
    @SerialName("treasuryname")
    val treasuryname: String,
    @SerialName("type")
    val type: Int,
    @SerialName("uid")
    val uid: Int,
    @SerialName("wrongactive")
    val wrongactive: Int
)