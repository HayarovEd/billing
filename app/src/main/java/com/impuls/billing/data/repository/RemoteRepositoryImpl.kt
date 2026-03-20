package com.impuls.billing.data.repository

import com.impuls.billing.domain.repository.RemoteRepository
import io.ktor.client.HttpClient

class RemoteRepositoryImpl(
    private val httpClient: HttpClient
): RemoteRepository {
}