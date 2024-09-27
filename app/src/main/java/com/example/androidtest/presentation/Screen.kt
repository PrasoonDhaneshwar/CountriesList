package com.example.androidtest.presentation

sealed class Screen(val route: String) {
    data object CountryListScreen: Screen("country_list_screen")
    data object ExchangeRateScreen: Screen("exchange_rate_screen")
}