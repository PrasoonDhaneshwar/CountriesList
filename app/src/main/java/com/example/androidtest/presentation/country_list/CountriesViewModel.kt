package com.example.androidtest.presentation.country_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtest.common.Resource
import com.example.androidtest.domain.use_case.get_countries.GetCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
) : ViewModel() {

    private val TAG = "CountriesViewModel"
    // State for countries data with Resource
    private val _countries = MutableStateFlow(CountriesListState())
    val countries = _countries
        .onStart {
            getCountries()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CountriesListState()
        )

    fun getCountries() {
        getCountriesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "getCountriesUseCase(), Resource.Success: ${result.data.toString()}")
                    _countries.value = CountriesListState(countries = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    Log.d(TAG, "getCountriesUseCase(), Resource.Error: ${result.data}, Error: ${result.message}")
                    _countries.value =
                        CountriesListState(error = result.message ?: "An unexpected error occurred")

                }

                is Resource.Loading -> {
                    Log.d(TAG, "getCountriesUseCase(), Resource.Loading: ${result.data}")
                    _countries.value = CountriesListState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(scope = viewModelScope)
    }
}