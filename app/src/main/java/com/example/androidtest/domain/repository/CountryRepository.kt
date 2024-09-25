package com.example.androidtest.domain.repository

import com.example.androidtest.countries.CountriesApi
import com.example.androidtest.countries.model.CountriesResponse
import javax.inject.Inject

class CountryRepository @Inject constructor(private val countriesApi: CountriesApi){
    suspend fun getCountries(): List<CountriesResponse> {
        return countriesApi.fetchCountriesFullList()
    }
}