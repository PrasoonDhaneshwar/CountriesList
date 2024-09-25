package com.example.androidtest.presentation.country_list

import com.example.androidtest.domain.model.Country

data class CountriesListState(
    val isLoading: Boolean = false,
    val countries: List<Country> = emptyList(),
    val error: String = ""
)
