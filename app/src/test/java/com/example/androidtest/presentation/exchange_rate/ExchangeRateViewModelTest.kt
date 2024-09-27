package com.example.androidtest.presentation.exchange_rate

import FakeGetExchangeRateUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.androidtest.domain.model.ExchangeRate
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ExchangeRateViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ExchangeRateViewModel
    private lateinit var fakeGetExchangeRateUseCase: FakeGetExchangeRateUseCase
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var testScope: TestScope
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)

        // Use a valid JSON string representation for the Country with all required fields
        val validCountryJson = """
            {
                "name": "Fake Country",
                "currencies": {
                    "BBD": { "name": "Barbadian Dollar", "symbol": "$" }
                },
                "flagUrl": "http://example.com/flag.png"
            }
        """.trimIndent()

        // Mock SavedStateHandle to return the valid JSON string
        savedStateHandle = mockk(relaxed = true)
        every { savedStateHandle.get<String>("countryJson") } returns validCountryJson

        // Initialize the fake use case
        fakeGetExchangeRateUseCase = FakeGetExchangeRateUseCase()
        viewModel = ExchangeRateViewModel(fakeGetExchangeRateUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getExchangeRate returns success`() = testScope.runTest {
        val exchangeRate = ExchangeRate(base = "BBD", rates = mapOf("USD" to 0.5))
        fakeGetExchangeRateUseCase.setExchangeRateResponse(exchangeRate)

        // Collect state flow, limiting to a specific number of emissions
        val states = mutableListOf<ExchangeRateState>()
        val job = launch {
            viewModel.exchangeRate.take(3).collect { states.add(it)
                println(states)
            }
        }

        // Advance the dispatcher to allow emissions
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        // Validate the states collected
        assertEquals(2, states.size)
        assertTrue(states[0].isLoading)
        assertNotNull(states[1].exchange)
        assertEquals("BBD", states[1].exchange?.base)
        assertEquals(0.5, states[1].exchange?.rates?.get("USD"))
    }

    @Test
    fun `test getExchangeRate returns error`() = testScope.runTest {
        fakeGetExchangeRateUseCase.setShouldReturnError(true)

        // Collect state flow, limiting to a specific number of emissions
        val states = mutableListOf<ExchangeRateState>()
        val job = launch {
            viewModel.exchangeRate.collect { states.add(it) }
        }

        // Advance the dispatcher to allow emissions
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        // Validate the states collected
        assertEquals(2, states.size) // Check for Loading, Loading, Error
        assertTrue(states[0].isLoading) // Ensure the second state is loading
        assertEquals("An error occurred", states[1].error) // Ensure the last state has the error message
    }

    @Test
    fun `test viewModel initializes with countryJson`() {
        assertNotNull(viewModel.exchangeRate)
    }
}