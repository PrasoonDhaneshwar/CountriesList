package com.prasoon.exchangerate

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@ExperimentalCoroutinesApi
class ExchangeRateApiTest {

    private lateinit var mockExchangeRateApi: ExchangeRateApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        @OptIn(ExperimentalSerializationApi::class)
        val json by lazy {
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
                // explicitNulls, defines whether null property
                // values should be included in the serialized JSON string.
                explicitNulls = false

                // If API returns null or unknown values for Enums, we can use default constructor parameter to override it
                // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/json.md#coercing-input-values
                coerceInputValues = true
            }
        }

        val contentType = "application/json; charset=utf-8".toMediaType()

        // Initialize MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Create a Retrofit instance using the mock server's URL
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use the mock server's URL
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        // Create the ExchangeRateApi instance
        mockExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
    }

    @Test
    fun `fetchExchangeRate returns valid response`() = runBlocking {
        // Mock the response for the specific currency
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{ \"base\": \"USD\", \"rates\": { \"EUR\": 0.85 }}") // Mocked response

        // Enqueue the mock response
        mockWebServer.enqueue(mockResponse)

        // Act: Call the fetchExchangeRate method
        val response = mockExchangeRateApi.fetchExchangeRate("USD")

        // Assert: Validate the response
        assertNotNull(response)
        assertEquals("USD", response.base)
        assertEquals(0.85, response.rates["EUR"])
    }

    @After
    fun tearDown() {
        // Shutdown MockWebServer
        mockWebServer.shutdown()
    }
}