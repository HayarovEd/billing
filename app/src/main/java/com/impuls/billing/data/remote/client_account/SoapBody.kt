package com.impuls.billing.data.remote.client_account

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Body", "http://schemas.xmlsoap.org/soap/envelope/")
data class SoapBody(
    @XmlSerialName("getClientAccountResponse", "urn:api3")
    val response: GetClientAccountResponse
)
