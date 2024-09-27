package com.prasoon.exchangerate

import com.prasoon.exchangerate.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ExchangeRateApi {
    @GET("{currency}")
    suspend fun fetchExchangeRate(@Path("currency") currency: String): ExchangeRateResponse
}