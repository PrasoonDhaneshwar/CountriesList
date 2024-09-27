package com.example.androidtest.presentation.exchange_rate

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.androidtest.R
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.ExchangeRate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateScreen(
    state: ExchangeRateState,
    navController: NavController,
    encodedCountryString: String,
) {
    val TAG = "ExchangeRateScreen"

    Log.d(TAG, "encodedCountry received: $encodedCountryString")
    val decodedCountryJsonString =
        URLDecoder.decode(encodedCountryString, StandardCharsets.UTF_8.toString())
    val country = Json.decodeFromString<Country>(decodedCountryJsonString)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.country_details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Content of the screen goes here, with padding applied from the Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display country flag
            SubcomposeAsyncImage(
                model = country.flagUrl,
                contentDescription = "Country Flag",
                loading = { CircularProgressIndicator() },
                modifier = Modifier
                    .height(120.dp)
                    .width(180.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
            // Country Name
            Text(
                text = country.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            // Currency Name and Symbol from Country object
            val currencyInfo =
                country.currencies?.values?.firstOrNull() // Get the first currency available
            if (currencyInfo != null) {
                Text(
                    text = buildString {
                        append(stringResource(R.string.currency))
                        append(" ")
                        append(currencyInfo.name)
                        append(" (")
                        append(currencyInfo.symbol)
                        append(")")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.currency_not_available),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            // Currency Name and Symbol
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.testTag("loadingIndicator"))
            } else if (state.error.isNotEmpty()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            } else {
                Log.d(TAG, "state.exchange: ${state.exchange}")

                state.exchange?.let { exchangeRate ->
                    val usdRate = exchangeRate.rates["USD"]  // Extract the USD rate
                    // Display Exchange Rate to USD
                    Text(
                        text = if (usdRate != null) {
                            buildString {
                                append(stringResource(R.string.usd_exchange_rate))
                                append(" ")
                                append(usdRate)
                            }
                        } else {
                            stringResource(
                                R.string.usd_exchange_rate_not_available,
                                exchangeRate.base
                            )
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (state.error.isNotBlank()) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExchangeRateScreenPreview() {
    // Mocking a Country object
    val mockCountry = Country(
        name = "Germany",
        currencies = mapOf(
            "EUR" to Currency(name = "Euro", symbol = "€")
        ),
        flagUrl = "https://flagcdn.com/w320/de.png"
    )

    // Mocking ViewModel State
    val mockState = ExchangeRateState(
        exchange = ExchangeRate(
            base = "EUR",
            rates = mapOf("USD" to 0.9)
        )
    )

    // Mock NavController
    val mockNavController = rememberNavController()

    // Encode mockCountry to JSON and URL-encode it for navigation
    val countryJson = Json.encodeToString(mockCountry)
    val encodedCountryJson = URLEncoder.encode(countryJson, StandardCharsets.UTF_8.toString())

    // Call the ExchangeRateScreen Composable with the mock data
    ExchangeRateScreen(
        mockState,
        navController = mockNavController,
        encodedCountryString = encodedCountryJson,
    )
}