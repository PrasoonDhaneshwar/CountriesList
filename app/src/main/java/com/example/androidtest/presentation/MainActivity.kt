package com.example.androidtest.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtest.countries.service.CountriesService
import com.example.androidtest.domain.model.Country
import com.example.androidtest.presentation.country_list.CountriesViewModel
import com.example.androidtest.presentation.country_list.CountryListScreen
import com.example.androidtest.presentation.exchange_rate.ExchangeRateScreen
import com.example.androidtest.presentation.exchange_rate.ExchangeRateViewModel
import com.example.androidtest.presentation.ui.theme.AndroidTheme
import com.prasoon.exchangerate.service.ExchangeRateService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CountryListScreen.route
                    ) {
                        composable(
                            route = Screen.CountryListScreen.route
                        ) {
                            val viewModel = hiltViewModel<CountriesViewModel>()
                            val state by viewModel.countries.collectAsStateWithLifecycle()
                            CountryListScreen(state, navController)
                        }
                        composable(
                            route = Screen.ExchangeRateScreen.route + "/{countryJson}",
                            arguments = listOf(navArgument("countryJson") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            // Deserialize the country string back to a Country object
                            val countryJson = backStackEntry.arguments?.getString("countryJson")
                            Log.d(TAG, "countryJson: ${countryJson.toString()}")

                            //val country = Json.decodeFromString<Country>(countryJson!!)
                            countryJson?.let {
                            val viewModel = hiltViewModel<ExchangeRateViewModel>()
                            val state by viewModel.exchangeRate.collectAsStateWithLifecycle()
                                ExchangeRateScreen(state, navController, it)
                            }
                        }
                    }
                }
            }
        }
    }
}
