package com.impuls.billing.data.remote.client_account


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgreementDto(
    @SerialName("agrmid")
    val agrmid: Long,
    @SerialName("BCheck")
    val bCheck: String,
    @SerialName("BNotify")
    val bNotify: Long,
    @SerialName("balance")
    val balance: Double,
    @SerialName("balancestatus")
    val balancestatus: Long,
    @SerialName("bcheck")
    val bcheck: String,
    @SerialName("blockamount")
    val blockamount: Double,
    @SerialName("blockdays")
    val blockdays: Long,
    @SerialName("blockmonths")
    val blockmonths: Long,
    @SerialName("blockorders")
    val blockorders: Long,
    @SerialName("bnotify")
    val bnotify: Long,
    @SerialName("closedon")
    val closedon: String,
    @SerialName("code")
    val code: String,
    @SerialName("credit")
    val credit: Double,
    @SerialName("curid")
    val curid: Long,
    @SerialName("date")
    val date: String,
    @SerialName("friendagrmid")
    val friendagrmid: Long,
    @SerialName("initialbalance")
    val initialbalance: Double,
    @SerialName("installments")
    val installments: Double,
    @SerialName("isdefault")
    val isdefault: Long,
    @SerialName("monthblockday")
    val monthblockday: Long,
    @SerialName("number")
    val number: String,
    @SerialName("operid")
    val operid: Long,
    @SerialName("orderpayday")
    val orderpayday: Long,
    @SerialName("ownerid")
    val ownerid: Long,
    @SerialName("paymentmethod")
    val paymentmethod: Long,
    @SerialName("penaltymethod")
    val penaltymethod: Long,
    @SerialName("promisecredit")
    val promisecredit: Double,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("uid")
    val uid: Long,
    @SerialName("username")
    val username: String,
    @SerialName("VGroups")
    val vGroups: Long,
    @SerialName("vgroups")
    val vgroups: Long
)