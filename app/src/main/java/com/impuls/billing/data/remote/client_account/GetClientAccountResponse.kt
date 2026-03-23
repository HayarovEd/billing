package com.impuls.billing.data.remote.client_account

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("getClientAccountResponse", "urn:api3")
data class GetClientAccountResponse(
    val ret: ClientAccountDataDto
)