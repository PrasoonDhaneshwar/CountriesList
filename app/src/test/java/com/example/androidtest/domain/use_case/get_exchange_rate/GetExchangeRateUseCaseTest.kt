package com.example.androidtest.domain.use_case.get_exchange_rate

import com.example.androidtest.common.Resource
import com.example.androidtest.domain.model.ExchangeRate
import com.example.androidtest.domain.repository.ExchangeRateRepository
import com.prasoon.exchangerate.model.ExchangeRateResponse
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetExchangeRateUseCaseTest {

    private lateinit var getExchangeRateUseCase: GetExchangeRateUseCase
    private lateinit var fakeExchangeRateRepository: ExchangeRateRepository

    @Before
    fun setUp() {
        // Initialize the fake repository
        fakeExchangeRateRepository = mockk()
        getExchangeRateUseCase = GetExchangeRateUseCase(fakeExchangeRateRepository)
    }

    @Test
    fun `invoke returns exchange rate successfully`() = runTest {
        // Arrange
        val currency = "USD"
        val mockResponse = ExchangeRateResponse(base = currency, rates = mapOf("EUR" to 0.85))
        val expectedExchangeRate = ExchangeRate(base = currency, rates = mapOf("EUR" to 0.85))

        // Mock the repository call
        coEvery { fakeExchangeRateRepository.getCountryExchangeRate(currency) } returns mockResponse

        // Act
        val flow = getExchangeRateUseCase(currency)

        // Collect the emitted values
        val emittedValues = mutableListOf<Resource<ExchangeRate>>()
        flow.collect { emittedValues.add(it) }

        // Assert
        assertEquals(2, emittedValues.size) // Loading, Success
        assertTrue(emittedValues[0] is Resource.Loading)
        assertTrue(emittedValues[1] is Resource.Success)
        assertEquals(expectedExchangeRate, (emittedValues[1] as Resource.Success).data)
    }

    @Test
    fun `invoke handles HttpException`() = runTest {
        // Arrange
        val currency = "USD"
        val errorMessage = "Not Found"

        // Create a mock response body to return with the HttpException
        val responseBody = mockk<ResponseBody>(relaxed = true) // Relaxed so that you don't have to stub every method
        every { responseBody.string() } returns errorMessage // Stubbing the string method to return your error message

        // Mock the repository to throw HttpException with the mock response body
        coEvery { fakeExchangeRateRepository.getCountryExchangeRate(currency) } throws HttpException(
            Response.error<String>(404, responseBody)
        )

        // Act
        val flow = getExchangeRateUseCase(currency)

        // Collect the emitted values
        val emittedValues = mutableListOf<Resource<ExchangeRate>>()
        flow.collect { emittedValues.add(it) }

        // Assert
        assertEquals(2, emittedValues.size) // Loading, Error
        assertTrue(emittedValues[0] is Resource.Loading)
        assertTrue(emittedValues[1] is Resource.Error)
        assertEquals("HTTP 404 Response.error()", (emittedValues[1] as Resource.Error).message)
    }

    @Test
    fun `invoke handles IOException`() = runTest {
        // Arrange
        val currency = "USD"

        // Mock the repository to throw IOException
        coEvery { fakeExchangeRateRepository.getCountryExchangeRate(currency) } throws IOException("Network Error")

        // Act
        val flow = getExchangeRateUseCase(currency)

        // Collect the emitted values
        val emittedValues = mutableListOf<Resource<ExchangeRate>>()
        flow.collect { emittedValues.add(it) }

        // Assert
        assertEquals(2, emittedValues.size) // Loading, Error
        assertTrue(emittedValues[0] is Resource.Loading)
        assertTrue(emittedValues[1] is Resource.Error)
        assertEquals("Couldn't reach server. Check your internet connection.", (emittedValues[1] as Resource.Error).message)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}