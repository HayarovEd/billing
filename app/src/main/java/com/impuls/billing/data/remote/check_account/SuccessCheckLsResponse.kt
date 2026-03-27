package com.impuls.billing.data.remote.check_account


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuccessCheckLsResponse(
    @SerialName("result")
    val result: ResultResponse
)