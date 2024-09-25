package com.example.androidtest.domain.model

import com.prasoon.exchangerate.model.ExchangeRateResponse

data class ExchangeRate(
    var base: String,
    var rates: Map<String, Double>
)

fun ExchangeRateResponse.toExchangeRateUI(): ExchangeRate {
    return ExchangeRate(
        base = base,
        rates = rates.toMutableMap()
    )
}