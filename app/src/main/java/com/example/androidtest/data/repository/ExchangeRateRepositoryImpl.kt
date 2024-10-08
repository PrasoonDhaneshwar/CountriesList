package com.example.androidtest.data.repository

import com.example.androidtest.domain.repository.ExchangeRateRepository
import com.prasoon.exchangerate.ExchangeRateApi
import com.prasoon.exchangerate.model.ExchangeRateResponse
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(private val countryExchangeApi: ExchangeRateApi):
    ExchangeRateRepository {
    override suspend fun getCountryExchangeRate(currency: String): ExchangeRateResponse {
        return countryExchangeApi.fetchExchangeRate(currency)
    }
}