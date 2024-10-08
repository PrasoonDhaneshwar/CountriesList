package com.example.androidtest.data.repository

import com.example.androidtest.countries.CountriesApi
import com.example.androidtest.countries.model.CountriesResponse
import com.example.androidtest.countries.model.CountryName
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.toCountryUI
import com.example.androidtest.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(private val countriesApi: CountriesApi) : CountryRepository{
    override suspend fun getCountries(): List<CountriesResponse> {
        return countriesApi.fetchCountriesFullList()
    }

    override suspend fun getCountry(countryName: String): Country? {
        return countriesApi.fetchCountriesFullList().map { it.toCountryUI() }.find { it.name == countryName }
    }
}