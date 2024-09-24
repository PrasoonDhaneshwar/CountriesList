package com.example.androidtest.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtest.common.Resource
import com.example.androidtest.countries.model.CountriesResponse
import com.example.androidtest.countries.service.CountriesService
import com.prasoon.exchangerate.service.ExchangeRateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CountriesViewModel: ViewModel() {

    private val countriesService = CountriesService()
    private val exchangeRateService = ExchangeRateService() // New service for exchange rates

    // State for countries data with Resource
    private val _countries = MutableStateFlow<Resource<List<CountriesResponse>>>(Resource.Loading)
    val countries: StateFlow<Resource<List<CountriesResponse>>> = _countries

    // Holds the exchange rate for selected country
    private val _exchangeRate = MutableStateFlow<Double?>(null)
    val exchangeRate: StateFlow<Double?> = _exchangeRate

    // Error handling
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Fetch all countries
    fun getCountries() {
        viewModelScope.launch {
            _countries.value = Resource.Loading // Indicate loading state
            try {
                val response = countriesService.countriesApi.fetchCountriesFullList()
                if (response.isSuccessful) {
                    _countries.value = Resource.Success(response.body() ?: emptyList())
                    Log.d("CountriesViewModel", "${_countries.value}")
                } else {
                    _countries.value = Resource.Failure(Exception("Failed to fetch countries: ${response.message()}"))
                }
            } catch (e: Exception) {
                _countries.value = Resource.Failure(e)
            }
        }
    }

    // Fetch exchange rate for a country
    fun getExchangeRate(currencyCode: String) {
        viewModelScope.launch {
            try {
                val response = exchangeRateService.exchangeRateApi.fetchExchangeRate(currencyCode)
                if (response.isSuccessful) {
                    _exchangeRate.value = response.body()?.rates?.get("USD")
                } else {
                    _error.value = "No exchange rate data available"
                }
            } catch (e: Exception) {
                _error.value = "Exception occurred: ${e.localizedMessage}"
            }
        }
    }

    // Clean up the scope when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}