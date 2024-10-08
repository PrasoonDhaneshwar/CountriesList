package com.example.androidtest.domain.repository

import com.prasoon.exchangerate.model.ExchangeRateResponse

interface ExchangeRateRepository {
    suspend fun getCountryExchangeRate(currency: String): ExchangeRateResponse
}