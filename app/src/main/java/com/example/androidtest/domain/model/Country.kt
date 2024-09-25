package com.example.androidtest.domain.model

import com.example.androidtest.countries.model.CountriesResponse
import com.example.androidtest.countries.model.CountryName
import com.example.androidtest.countries.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val name: String,
    val currencies: Map<String, Currency>?,
    val flagUrl: String
)

fun CountriesResponse.toCountryUI(): Country {
    return Country(
        name = name.common,
        currencies = currencies?.toMutableMap(),
        flagUrl = flags.png
    )
}