package com.example.androidtest.presentation.exchange_rate

import com.example.androidtest.domain.model.ExchangeRate

data class ExchangeRateState(
    val isLoading: Boolean = false,
    val exchange: ExchangeRate? = null,
    val error: String = ""
)
