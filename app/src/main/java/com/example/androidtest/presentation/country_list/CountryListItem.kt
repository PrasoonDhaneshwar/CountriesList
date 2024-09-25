package com.example.androidtest.presentation.country_list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.androidtest.R
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import com.example.androidtest.presentation.Screen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CountryListItem(country: Country, navController: NavController) {
    val TAG = "CountryListItem"

    val currency = country.currencies?.values?.firstOrNull()
    val currencyText = currency?.let { "${it.name} (${it.symbol})" }
        ?: stringResource(R.string.currency_not_available)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { // Serialize the country object and navigate
                val countryString = Json.encodeToString(country)
                Log.d(TAG, "Country converted to String: $countryString")
                val encodedCountryString =
                    URLEncoder.encode(countryString, StandardCharsets.UTF_8.toString())
                navController.navigate(Screen.ExchangeRateScreen.route + "/${encodedCountryString}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Flag Image
        SubcomposeAsyncImage(
            model = country.flagUrl,
            contentDescription = "Flag of ${country.name}",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Green
                )
            },
            error = {
                // Error content if image loading fails
                Box(modifier = Modifier.size(40.dp)) {
                    Text(stringResource(R.string.error)) // todo
                }
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Country Name
            Text(
                text = country.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Currency Name and Symbol
            Text(
                text = currencyText,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun PreviewCountriesItemScreen() {
    val sampleCountry = Country(
        name = "South Georgia",
        currencies = mapOf("SHP" to Currency("Saint Helena pound", "£")),
        flagUrl = "https://flagcdn.com/w320/gs.png"
    )
    // Mock NavController
    val mockNavController = rememberNavController()

    CountryListItem(sampleCountry, navController = mockNavController)
}