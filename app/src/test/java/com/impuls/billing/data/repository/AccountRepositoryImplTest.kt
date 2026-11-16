package com.impuls.billing.data.repository

import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountRepositoryImplTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private fun createMockClient(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine { request ->
            handler(request)
        }) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Test
    fun `getClientAccount returns Success when API returns 200`() {
        runTest {
            val mockResponseBody = """
                {
                    "billDeliveryName": "Email",
                    "account": {
                        "uid": 123,
                        "name": "Test User",
                        "login": "test",
                        "pass": "pass",
                        "abonentname": "",
                        "abonentpatronymic": "",
                        "abonentsurname": "",
                        "archive": 0,
                        "bankname": "",
                        "bik": "",
                        "billdelivery": 1,
                        "branchbankname": "",
                        "category": 1,
                        "corr": "",
                        "descr": "",
                        "doctype": 1,
                        "email": "",
                        "emailisconfirmed": false,
                        "fax": "",
                        "inn": "",
                        "kpp": "",
                        "mobile": "",
                        "mobileisconfirmed": false,
                        "offerisaccepted": false,
                        "ogrn": "",
                        "okpo": "",
                        "oksm": 1,
                        "okved": "",
                        "ownership": 1,
                        "phone": "",
                        "settl": "",
                        "soleproprietor": false,
                        "treasuryaccount": "",
                        "treasuryname": "",
                        "type": 1,
                        "wrongactive": 0
                    },
                    "agreements": []
                }
            """.trimIndent()

            val client = createMockClient { _ ->
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString())
                )
            }

            val repository = AccountRepositoryImpl(client)
            val result = repository.getClientAccount("token")

            assertTrue(result is ResultWork.Success)
            assertEquals(123L, (result as ResultWork.Success).data.account.uid)
            assertEquals("Test User", result.data.account.name)
        }
    }

    @Test
    fun `getClientAccount returns Unauthorized error when API returns 401`() {
        runTest {
            val client = createMockClient { _ ->
                respond(
                    content = "",
                    status = HttpStatusCode.Unauthorized,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString())
                )
            }

            val repository = AccountRepositoryImpl(client)
            val result = repository.getClientAccount("token")

            assertTrue(result is ResultWork.Error)
            assertEquals(DataError.Network.UNAUTHORIZED, (result as ResultWork.Error).error)
        }
    }

    @Test
    fun `setClientMobilePhone returns true when API returns success`() {
        runTest {
            val mockResponseBody = """
                {
                    "success": true
                }
            """.trimIndent()

            val client = createMockClient { _ ->
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString())
                )
            }

            val repository = AccountRepositoryImpl(client)
            val result = repository.setClientMobilePhone("token", 123L, "1234567890")

            assertTrue(result is ResultWork.Success)
            assertEquals(true, (result as ResultWork.Success).data)
        }
    }

    @Test
    fun `getClientInfo maps data correctly`() {
        runTest {
            val mockResponseBody = """
                {
                    "email": "test@test.com",
                    "mobile": "1234567890",
                    "emailIsConfirmed": true,
                    "mobileIsConfirmed": false,
                    "kontPerson": "John Doe",
                    "offerIsAccepted": true,
                    "passIsTemporary": false,
                    "password": "hashed_password",
                    "phone": "9876543210",
                    "triggerContactDataReminder": false
                }
            """.trimIndent()

            val client = createMockClient { _ ->
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString())
                )
            }

            val repository = AccountRepositoryImpl(client)
            val result = repository.getClientInfo("token")

            assertTrue(result is ResultWork.Success)
            val data = (result as ResultWork.Success).data
            assertEquals("test@test.com", data.email)
            assertEquals("1234567890", data.mobile)
            assertEquals(true, data.emailIsConfirmed)
            assertEquals(false, data.mobileIsConfirmed)
        }
    }

    @Test
    fun `getClientSharedPosts returns list and sends date parameter`() {
        runTest {
            val mockResponseBody = """
                [
                    {
                        "groupId": 1,
                        "pid": 10,
                        "postCatId": 100,
                        "postTime": "2026-07-29 15:00:00",
                        "status": 1,
                        "subject": "Subject",
                        "text": "Body",
                        "uid": 123,
                        "dtFrom": "",
                        "dtTo": "",
                        "emails": "",
                        "externUrl": "",
                        "postBy": 1,
                        "postTo": 1,
                        "sentTime": ""
                    }
                ]
            """.trimIndent()

            val client = createMockClient { request ->
                val dateParam = request.url.parameters["date_from"]
                assertEquals("2026-07-29", dateParam)
                
                respond(
                    content = mockResponseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString())
                )
            }

            val repository = AccountRepositoryImpl(client)
            val result = repository.getClientSharedPosts("token", kotlinx.datetime.LocalDate(2026, 7, 29))

            assertTrue(result is ResultWork.Success)
            val data = (result as ResultWork.Success).data
            assertEquals(1, data.size)
            assertEquals("Subject", data[0].subject)
        }
    }
}
