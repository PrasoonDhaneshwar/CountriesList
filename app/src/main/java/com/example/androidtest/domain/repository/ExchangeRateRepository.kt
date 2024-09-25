package com.example.androidtest.domain.repository

import com.prasoon.exchangerate.ExchangeRateApi
import com.prasoon.exchangerate.model.ExchangeRateResponse
import javax.inject.Inject

class ExchangeRateRepository @Inject constructor(private val countryExchangeApi: ExchangeRateApi){
    suspend fun getCountryExchangeRate(currency: String): ExchangeRateResponse {
        return countryExchangeApi.fetchExchangeRate(currency)
    }
}