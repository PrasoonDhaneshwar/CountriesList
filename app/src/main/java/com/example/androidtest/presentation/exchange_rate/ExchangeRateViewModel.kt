package com.example.androidtest.presentation.exchange_rate

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtest.common.Constants.COUNTRY_ID
import com.example.androidtest.common.Resource
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.use_case.get_exchange_rate.GetExchangeRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    savedStateHandle: SavedStateHandle  // Use it to save the state of app
) : ViewModel() {

    // State for countries data with Resource
    private val _exchangeRate = MutableStateFlow(ExchangeRateState())
    val exchangeRate = _exchangeRate.asStateFlow()

    init {
    // Fetch the country JSON string from SavedStateHandle, decode it, and use the currency code
        savedStateHandle.get<String>("countryJson")?.let { countryJson ->
            val decodedCountryJson =
                URLDecoder.decode(countryJson, StandardCharsets.UTF_8.toString())
            val country = Json.decodeFromString<Country>(decodedCountryJson)
            country.currencies?.let { currenciesMap ->
                val currencyCode = currenciesMap.keys.firstOrNull()
                currencyCode?.let { getExchangeRate(it) }
            }
        }
    }

    private fun getExchangeRate(currencyCode: String) {
        getExchangeRateUseCase(currencyCode).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    this._exchangeRate.value = ExchangeRateState(exchange = result.data)
                }

                is Resource.Error -> {
                    this._exchangeRate.value =
                        ExchangeRateState(error = result.message ?: "An unexpected error occurred")

                }

                is Resource.Loading -> {
                    this._exchangeRate.value = ExchangeRateState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    // Fetch exchange rate for a country
//    fun getExchangeRate(currencyCode: String) {
//        viewModelScope.launch {
//            try {
//                val response = exchangeRateService.exchangeRateApi.fetchExchangeRate(currencyCode)
//                if (response.isSuccessful) {
//                    this@ExchangeRateViewModel._exchangeRate.value = response.body()?.rates?.get("USD")
//                } else {
//                    _error.value = "No exchange rate data available"
//                }
//            } catch (e: Exception) {
//                _error.value = "Exception occurred: ${e.localizedMessage}"
//            }
//        }
//    }

    // Clean up the scope when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}