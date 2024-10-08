package com.example.androidtest.domain.repository

import com.example.androidtest.countries.model.CountriesResponse
import com.example.androidtest.domain.model.Country

interface CountryRepository {
    suspend fun getCountries(): List<CountriesResponse>

    suspend fun getCountry(countryName: String): Country?
}