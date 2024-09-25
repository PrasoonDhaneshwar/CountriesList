package com.example.androidtest.presentation.country_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.androidtest.R
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country

@Composable
fun CountryListScreen(
    state: CountriesListState,
    navController: NavController,
) {
    Column {
        Text(
            text = stringResource(R.string.countries_list),
            fontWeight = FontWeight(900),
            fontFamily = FontFamily.Cursive,
            fontSize = 32.sp,
            modifier = Modifier.padding(2.dp)
        )
        HorizontalDivider(color = Color.Gray)
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)) {
                items(state.countries) { country: Country ->
                    CountryListItem(
                        country = country,
                        navController
                    )
                    HorizontalDivider(color = Color.LightGray)
                }
            }
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
@Preview
@Composable
fun PreviewCountriesListScreen() {
    val sampleCountries = CountriesListState(
        isLoading = false,
        countries = listOf(
            Country(
                name = "South Georgia",
                currencies = mapOf("SHP" to Currency("Saint Helena pound", "£")),
                flagUrl = "https://flagcdn.com/w320/gs.png"
            ),
            Country(
                name = "Switzerland",
                currencies = mapOf("CHF" to Currency("Swiss franc", "Fr.")),
                flagUrl = "https://flagcdn.com/w320/ch.png"
            ),
            Country(
                name = "Tunisia",
                currencies = mapOf("TND" to Currency("Tunisian dinar", "د.ت")),
                flagUrl = "https://flagcdn.com/w320/tn.png"
            )
        )
    )
    // Mock NavController
    val mockNavController = rememberNavController()
    CountryListScreen(sampleCountries, mockNavController)
}