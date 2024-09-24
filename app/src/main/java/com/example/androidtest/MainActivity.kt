package com.example.androidtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.androidtest.countries.service.CountriesService
import com.example.androidtest.ui.theme.AndroidTheme
import com.prasoon.exchangerate.service.ExchangeRateService

class MainActivity : ComponentActivity() {

    private val countriesService = CountriesService()
    private val exchangeService = ExchangeRateService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidTheme {
                Scaffold { innerPadding ->
                    Text(
                        modifier = Modifier.padding(innerPadding),
                        text = "Let the games begin!"
                    )
                    LaunchedEffect(key1 = Unit) {
                        val character = countriesService.countriesApi.fetchCountriesFullList()
                        val response = exchangeService.exchangeRateApi.fetchExchangeRate("USD")

                        Log.d("MainActivity", character.toString())
                        Log.d("MainActivity", "currency: $response")
                    }
                }
            }
        }
    }
}
