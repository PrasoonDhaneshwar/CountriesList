package com.example.androidtest.countries.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias CurrencyCode = String

@Serializable
class CountriesResponse(
    @SerialName("name")
    val name: CountryName,
    @SerialName("currencies")
    val currencies: Map<CurrencyCode, Currency>?,
    @SerialName("flags")
    val flags: Flags
)

@Serializable
class Flags(
    @SerialName("png")
    val png: String,
    @SerialName("svg")
    val svg: String
)

@Serializable
class Currency(
    @SerialName("name")
    val name: String,
    @SerialName("symbol")
    val symbol: String
)

@Serializable
class CountryName(
    @SerialName("common")
    val common: String
)