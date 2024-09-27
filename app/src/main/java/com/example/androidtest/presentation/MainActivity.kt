package com.example.androidtest.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtest.presentation.country_list.CountriesViewModel
import com.example.androidtest.presentation.country_list.CountryListScreen
import com.example.androidtest.presentation.exchange_rate.ExchangeRateScreen
import com.example.androidtest.presentation.exchange_rate.ExchangeRateViewModel
import com.example.androidtest.presentation.ui.theme.AndroidTheme
import dagger.hilt.android.AndroidEntryPoint

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
                            val countryJsonString =
                                backStackEntry.arguments?.getString("countryJson")
                                    ?: "No Country available."
                            Log.d(TAG, "countryJsonString: $countryJsonString")

                            val viewModel = hiltViewModel<ExchangeRateViewModel>()
                            val state by viewModel.exchangeRate.collectAsStateWithLifecycle()
                            ExchangeRateScreen(state, navController, countryJsonString)
                        }
                    }
                }
            }
        }
    }
}