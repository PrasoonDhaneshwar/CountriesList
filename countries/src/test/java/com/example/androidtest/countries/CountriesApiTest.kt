package com.prasoon.exchangerate

import com.example.androidtest.countries.CountriesApi
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
class CountriesApiTest {

    private lateinit var mockCountriesApi: CountriesApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        @OptIn(ExperimentalSerializationApi::class)
        val json by lazy {
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
                explicitNulls = false
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

        // Create the CountriesApi instance
        mockCountriesApi = retrofit.create(CountriesApi::class.java)
    }

    @Test
    fun `fetchCountriesFullList returns valid response`() = runBlocking {
        // Mock the response for the countries API
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                [
                    {
                        "name": { "common": "Country1" },
                        "currencies": {
                            "USD": { "name": "US Dollar", "symbol": "$" }
                        },
                        "flags": {
                            "png": "http://example.com/flag1.png",
                            "svg": "http://example.com/flag1.svg"
                        }
                    },
                    {
                        "name": { "common": "Country2" },
                        "currencies": {
                            "EUR": { "name": "Euro", "symbol": "€" }
                        },
                        "flags": {
                            "png": "http://example.com/flag2.png",
                            "svg": "http://example.com/flag2.svg"
                        }
                    }
                ]
            """.trimIndent()) // Mocked response

        // Enqueue the mock response
        mockWebServer.enqueue(mockResponse)

        // Act: Call the fetchCountriesFullList method
        val response = mockCountriesApi.fetchCountriesFullList()

        // Assert: Validate the response
        assertNotNull(response)
        assertEquals(2, response.size)
        assertEquals("Country1", response[0].name.common)
        assertEquals("US Dollar", response[0].currencies?.get("USD")?.name)
        assertEquals("$", response[0].currencies?.get("USD")?.symbol)
        assertEquals("http://example.com/flag1.png", response[0].flags.png)
    }

    @After
    fun tearDown() {
        // Shutdown MockWebServer
        mockWebServer.shutdown()
    }
}