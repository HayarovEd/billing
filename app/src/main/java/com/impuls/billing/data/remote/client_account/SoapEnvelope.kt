package com.impuls.billing.data.remote.client_account

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Envelope", "http://schemas.xmlsoap.org/soap/envelope/")
data class SoapEnvelope(
    @XmlSerialName("Body", "http://schemas.xmlsoap.org/soap/envelope/")
    val body: SoapBody
)