package com.example.androidtest.presentation

sealed class Screen(val route: String) {
    object CountryListScreen: Screen("country_list_screen")
    object ExchangeRateScreen: Screen("exchange_rate_screen")
}