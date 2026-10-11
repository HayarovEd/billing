package com.impuls.billing.data.remote.set_client_info

data class SetClientInfoResponse(
    val ret: Int
)

// Или если нужно обернуть в полный SOAP ответ
data class SoapSetClientInfoResponse(
    val body: SetClientInfoBody
)

data class SetClientInfoBody(
    val setClientInfoResponse: SetClientInfoResponse
)