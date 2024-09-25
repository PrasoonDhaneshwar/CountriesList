package com.prasoon.exchangerate

import com.prasoon.exchangerate.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ExchangeRateApi {
    @GET("{currency}")
    suspend fun fetchExchangeRate(@Path("currency") currency: String): ExchangeRateResponse

}

//https://v6.exchangerate-api.com/v6/faf67eaa44adb5366c635c99/latest/USD

// old
//interface ExchangeRateApi {
//    // feel free to change the return type as you see fit as long as the body [ExchangeRateResponse]
//    // this api should use "https://api.exchangerate-api.com/v4/latest/{currencyCode}"
//    // e.g. https://api.exchangerate-api.com/v4/latest/USD
//    suspend fun getCurrencyRates(currencyCode: CurrencyCode): ExchangeRateResponse
//}
