package com.example.androidtest.exchangerate

import com.example.androidtest.countries.model.CurrencyCode
import com.example.androidtest.exchangerate.model.ExchangeRateResponse

interface ExchangeRateApi {
    // feel free to change the return type as you see fit as long as the body [ExchangeRateResponse]
    // this api should use "https://api.exchangerate-api.com/v4/latest/{currencyCode}"
    // e.g. https://api.exchangerate-api.com/v4/latest/USD
    suspend fun getCurrencyRates(currencyCode: CurrencyCode): ExchangeRateResponse
}
