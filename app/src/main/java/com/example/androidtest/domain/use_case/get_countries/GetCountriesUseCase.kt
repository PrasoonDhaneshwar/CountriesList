package com.example.androidtest.domain.use_case.get_countries

import android.util.Log
import com.example.androidtest.common.Resource
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.toCountryUI
import com.example.androidtest.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    private val TAG = "GetCountriesUseCase"
    operator fun invoke(): Flow<Resource<List<Country>>> = flow {
        try {
            emit(Resource.Loading<List<Country>>())
            val countries = repository.getCountries().map { it.toCountryUI() }
            Log.d(TAG, "Countries received: $countries")
            val countriesWithoutCurrencies = findCountriesWithoutCurrencies(countries)
            // todo to check which countries don't have currencies
            countriesWithoutCurrencies.forEach {
                Log.d(TAG, "countriesWithoutCurrencies: $it")
            }
            emit(Resource.Success<List<Country>>(countries))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Country>>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Country>>("Couldn't reach server. Check your internet connection."))
        }
    }

    fun findCountriesWithoutCurrencies(countries: List<Country>): List<String> {
        return countries.filter { it.currencies.isNullOrEmpty() }
            .map { it.name }
    }
}