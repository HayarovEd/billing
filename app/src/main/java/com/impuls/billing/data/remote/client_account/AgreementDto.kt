package com.impuls.billing.data.remote.client_account


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgreementDto(
    @SerialName("agrmid")
    val agrmid: Int,
    @SerialName("BCheck")
    val bCheck: String,
    @SerialName("BNotify")
    val bNotify: Int,
    @SerialName("balance")
    val balance: Double,
    @SerialName("balancestatus")
    val balancestatus: Int,
    @SerialName("bcheck")
    val bcheck: String,
    @SerialName("blockamount")
    val blockamount: Double,
    @SerialName("blockdays")
    val blockdays: Int,
    @SerialName("blockmonths")
    val blockmonths: Int,
    @SerialName("blockorders")
    val blockorders: Int,
    @SerialName("bnotify")
    val bnotify: Int,
    @SerialName("closedon")
    val closedon: String,
    @SerialName("code")
    val code: String,
    @SerialName("credit")
    val credit: Double,
    @SerialName("curid")
    val curid: Int,
    @SerialName("date")
    val date: String,
    @SerialName("friendagrmid")
    val friendagrmid: Int,
    @SerialName("initialbalance")
    val initialbalance: Double,
    @SerialName("installments")
    val installments: Double,
    @SerialName("isdefault")
    val isdefault: Int,
    @SerialName("monthblockday")
    val monthblockday: Int,
    @SerialName("number")
    val number: String,
    @SerialName("operid")
    val operid: Int,
    @SerialName("orderpayday")
    val orderpayday: Int,
    @SerialName("ownerid")
    val ownerid: Int,
    @SerialName("paymentmethod")
    val paymentmethod: Int,
    @SerialName("penaltymethod")
    val penaltymethod: Int,
    @SerialName("promisecredit")
    val promisecredit: Double,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("uid")
    val uid: Int,
    @SerialName("username")
    val username: String,
    @SerialName("VGroups")
    val vGroups: Int,
    @SerialName("vgroups")
    val vgroups: Int
)