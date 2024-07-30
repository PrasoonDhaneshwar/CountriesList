package com.example.androidtest.countries

import com.example.androidtest.countries.model.CountriesResponse
import retrofit2.Response
import retrofit2.http.GET


interface CountriesApi {
    @GET("all")
    suspend fun fetchCountriesFullList(): Response<List<CountriesResponse>>
}