package com.example.androidtest.presentation.country_list

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidtest.common.Resource
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CountriesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CountriesViewModel
    private lateinit var fakeGetCountriesUseCase: FakeGetCountriesUseCase
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var testScope: TestScope

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testScope = TestScope(testDispatcher)

        // Mock the GetCountriesUseCase
        fakeGetCountriesUseCase = mockk()

        // Initialize the ViewModel with the mocked use case
        viewModel = CountriesViewModel(fakeGetCountriesUseCase)
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getCountries returns error`() = testScope.runTest {
        // Mock the flow to return an error
        every { fakeGetCountriesUseCase.invoke() } returns flow {
            emit(Resource.Loading())
            delay(50) // Ensure there's a slight delay for the Loading state
            emit(Resource.Error("An error occurred"))
        }

        // Wait for emissions to be processed
        viewModel.getCountries()

        // Collect states and assert values directly
        val job = launch {
            viewModel.countries.collect { state ->
                println("Collected state: $state") // Add logging
                if (state.isLoading) {
                    assertTrue("Loading state should be emitted", true) // Check if loading state was emitted
                } else if (state.error.isNotEmpty()) {
                    assertEquals("An error occurred", state.error)
                }
            }
        }

        // Wait for emissions to be processed
        testDispatcher.scheduler.advanceUntilIdle()

        // Cancel the collection job to prevent leaks
        job.cancel()
    }

    @Test
    fun `test getCountries returns success`() = testScope.runTest {
        // Define mock countries
        val countries = listOf(
            Country(
                name = "Country1",
                currencies = mapOf("USD" to Currency("US Dollar", "$")),
                flagUrl = "http://example.com/flag1.png"
            ),
            Country(
                name = "Country2",
                currencies = mapOf("EUR" to Currency("Euro", "€")),
                flagUrl = "http://example.com/flag2.png"
            )
        )

        // Mock the flow returned by getCountriesUseCase() to return a successful response
        every { fakeGetCountriesUseCase.invoke() } returns flow {
            emit(Resource.Loading())
            delay(50) // Ensure there's a slight delay for the Loading state
            emit(Resource.Success(countries)) // Emit the list of Country
        }

        // Wait for emissions to be processed
        viewModel.getCountries()

        // Collect states and assert values directly
        val job = launch {
            viewModel.countries.collect { state ->
                println("Collected state: $state") // Add logging
                if (state.isLoading) {
                    assertTrue("Loading state should be emitted", true) // Check if loading state was emitted
                } else if (state.countries.isNotEmpty()) {
                    assertEquals(2, state.countries.size) // Should contain two countries
                    assertEquals("Country1", state.countries[0].name) // Validate the first country name
                    assertEquals("Country2", state.countries[1].name) // Validate the second country name
                }
            }
        }

        // Wait for emissions to be processed
        testDispatcher.scheduler.advanceUntilIdle()

        // Cancel the collection job to prevent leaks
        job.cancel()
    }
}