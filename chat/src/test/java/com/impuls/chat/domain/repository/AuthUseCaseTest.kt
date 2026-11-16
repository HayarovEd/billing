package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.Token
import com.impuls.chat.domain.model.UserBillingData
import com.impuls.chat.domain.model.UserData
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.JwtRepository
import com.impuls.chat.domain.utils.ResultWork
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthUseCaseTest {

    private val remoteRepository: RemoteChatRepository = mockk(relaxed = true)
    private val dataStoreRepository: DataStoreChatRepository = mockk(relaxed = true)
    private val serviceChatRepository: ServiceChatRepository = mockk(relaxed = true)

    private lateinit var authUseCase: AuthUseCase

    private val expiredAccessToken = "expired_access_token"
    private val validAccessToken = "valid_access_token"
    private val newAccessToken = "new_access_token"

    private val testBillingData = UserBillingData(
        name = "Test User",
        login = "test_login",
        password = "test_password",
        email = "test@test.com",
        billingToken = "billing_token"
    )

    @Before
    fun setUp() {
        mockkObject(JwtRepository)
        coEvery { JwtRepository.decodeJwt(expiredAccessToken) } returns UserData(
            userId = "123",
            name = "Test",
            role = "user",
            email = "test@test.com",
            expired = 100L
        )
        coEvery { JwtRepository.decodeJwt(validAccessToken) } returns UserData(
            userId = "123",
            name = "Test",
            role = "user",
            email = "test@test.com",
            expired = 2000000000L
        )

        authUseCase = AuthUseCase(
            repository = remoteRepository,
            dataStoreRepository = dataStoreRepository,
            serviceChatRepository = serviceChatRepository
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when token is valid, invokes function with existing access token and does not refresh`() = runTest {
        coEvery { dataStoreRepository.getStateToken() } returns Token(validAccessToken, "refresh_token")

        var functionCalled = false
        var passedToken: String? = null
        var errorCalled = false

        authUseCase.invoke(
            function = { token ->
                functionCalled = true
                passedToken = token
            },
            onError = { errorCalled = true }
        )

        assertTrue(functionCalled)
        assertEquals(validAccessToken, passedToken)
        assertFalse(errorCalled)
        coVerify(exactly = 0) { remoteRepository.onRefresh(any()) }
    }

    @Test
    fun `when refresh fails but onLoginFromApp succeeds, onError is NOT called and function is invoked with new token`() = runTest {
        coEvery { dataStoreRepository.getStateToken() } returns Token(expiredAccessToken, "expired_refresh_token")
        coEvery { remoteRepository.onRefresh("expired_refresh_token") } returns ResultWork.Error(ChatDataError.Network.UNAUTHORIZED)
        coEvery { dataStoreRepository.getUserBillingData() } returns testBillingData
        coEvery {
            remoteRepository.onLoginFromApp(
                name = any(),
                username = testBillingData.login,
                email = any(),
                password = testBillingData.password,
                token = testBillingData.billingToken
            )
        } returns ResultWork.Success(Token(newAccessToken, "new_refresh_token"))

        var functionCalled = false
        var passedToken: String? = null
        var errorCalled = false

        authUseCase.invoke(
            function = { token ->
                functionCalled = true
                passedToken = token
            },
            onError = { errorCalled = true }
        )

        assertFalse(errorCalled)
        assertTrue(functionCalled)
        assertEquals(newAccessToken, passedToken)
        coVerify { dataStoreRepository.saveTokens(Token(newAccessToken, "new_refresh_token")) }
    }

    @Test
    fun `when refresh fails and onLoginFromApp fails, onError IS called`() = runTest {
        coEvery { dataStoreRepository.getStateToken() } returns Token(expiredAccessToken, "expired_refresh_token")
        coEvery { remoteRepository.onRefresh("expired_refresh_token") } returns ResultWork.Error(ChatDataError.Network.UNAUTHORIZED)
        coEvery { dataStoreRepository.getUserBillingData() } returns testBillingData
        coEvery {
            remoteRepository.onLoginFromApp(
                name = any(),
                username = any(),
                email = any(),
                password = any(),
                token = any()
            )
        } returns ResultWork.Error(ChatDataError.Network.UNAUTHORIZED)

        var functionCalled = false
        var receivedError: ChatDataError? = null

        authUseCase.invoke(
            function = { functionCalled = true },
            onError = { error -> receivedError = error }
        )

        assertFalse(functionCalled)
        assertEquals(ChatDataError.Network.UNAUTHORIZED, receivedError)
    }
}
