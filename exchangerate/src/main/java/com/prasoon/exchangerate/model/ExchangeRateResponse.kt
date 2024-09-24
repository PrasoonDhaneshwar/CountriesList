package com.prasoon.exchangerate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponse(
    @SerialName("base")
    var base: String,
    @SerialName("rates")
    var rates: Map<String, Double>
)

// old
//@Serializable
//data class ExchangeRateResponse(
//    @SerialName("base")
//    var base: String,
//    @SerialName("rates")
//    var rates: Map<String, Double>
//)
