package com.example.androidtest.domain.use_case.get_countries

import com.example.androidtest.common.Resource
import com.example.androidtest.countries.model.CountriesResponse
import com.example.androidtest.countries.model.CountryName
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.countries.model.Flags
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.toCountryUI
import com.example.androidtest.domain.repository.CountryRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetCountriesUseCaseTest {

    private lateinit var getCountriesUseCase: GetCountriesUseCase
    private lateinit var fakeCountryRepository: CountryRepository

    @Before
    fun setUp() {
        // Initialize the fake repository
        fakeCountryRepository = mockk()
        getCountriesUseCase = GetCountriesUseCase(fakeCountryRepository)
    }

    @Test
    fun `invoke returns list of countries successfully`() = runTest {
        // Arrange
        val mockResponse = listOf(
            CountriesResponse(
                name = CountryName("Country1"),
                currencies = mapOf("USD" to Currency("US Dollar", "$")),
                flags = Flags("http://example.com/flag1.png")
            ),
            CountriesResponse(
                name = CountryName("Country2"),
                currencies = mapOf("EUR" to Currency("Euro", "€")),
                flags = Flags("http://example.com/flag2.png")
            )
        )

        val expectedCountries = mockResponse.map { it.toCountryUI() }

        // Mock the repository call
        coEvery { fakeCountryRepository.getCountries() } returns mockResponse

        // Act
        val flow = getCountriesUseCase()

        // Collect the emitted values
        val emittedValues = mutableListOf<Resource<List<Country>>>()
        flow.collect { emittedValues.add(it) }

        // Assert
        assertEquals(2, emittedValues.size) // Loading, Success
        assertTrue(emittedValues[0] is Resource.Loading)
        assertTrue(emittedValues[1] is Resource.Success)
        assertEquals(expectedCountries, (emittedValues[1] as Resource.Success).data)
    }

    @Test
    fun `invoke handles HttpException`() = runTest {
        // Arrange
        val errorMessage = "HTTP 404 Response.error()"

        // Mock the repository to throw HttpException
        coEvery { fakeCountryRepository.getCountries() } throws HttpException(
            Response.error<List<CountriesResponse>>(404, mockk(relaxed = true))
        )

        // Act
        val flow = getCountriesUseCase()

        // Collect the emitted values
        val emittedValues = mutableListOf<Resource<List<Country>>>()
        flow.collect { emittedValues.add(it) }

        // Assert
        assertEquals(2, emittedValues.size) // Loading, Error
        assertTrue(emittedValues[0] is Resource.Loading)
        assertTrue(emittedValues[1] is Resource.Error)
        assertEquals(errorMessage, (emittedValues[1] as Resource.Error).message)
    }

    @Test
    fun `invoke handles IOException`() = runTest {
        // Mock the repository to throw IOException
        coEvery { fakeCountryRepository.getCountries() } throws IOException("Network Error")

        // Act
        val flow = getCountriesUseCase()

        // Collect the emitted values
        val emittedValues = mutableListOf<Resource<List<Country>>>()
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