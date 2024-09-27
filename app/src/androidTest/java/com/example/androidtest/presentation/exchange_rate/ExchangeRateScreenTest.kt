package com.example.androidtest.presentation.exchange_rate

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.ExchangeRate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RunWith(AndroidJUnit4::class)
class ExchangeRateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testExchangeRateScreen_loadingState() {
        // Arrange
        val mockState = ExchangeRateState(isLoading = true)
        val mockCountry = Country(
            name = "Sierra Leone",
            flagUrl = "https://flagcdn.com/w320/sl.png",
            currencies = mapOf("SLL" to Currency(name = "Leone", symbol = "Le"))
        )
        val encodedCountry = URLEncoder.encode(Json.encodeToString(mockCountry), StandardCharsets.UTF_8.toString())

        // Act
        composeTestRule.setContent {
            ExchangeRateScreen(
                state = mockState,
                navController = rememberNavController(),
                encodedCountryString = encodedCountry
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Sierra Leone").assertExists()
        composeTestRule.onNodeWithText("Currency: Leone (Le)").assertExists()
        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }

    @Test
    fun testExchangeRateScreen_successState() {
        // Arrange
        val mockState = ExchangeRateState(
            exchange = ExchangeRate(base = "SLL", rates = mapOf("USD" to 4.4E-5))
        )
        val mockCountry = Country(
            name = "Sierra Leone",
            flagUrl = "https://flagcdn.com/w320/sl.png",
            currencies = mapOf("SLL" to Currency(name = "leone", symbol = "Le"))
        )
        val encodedCountry = URLEncoder.encode(Json.encodeToString(mockCountry), StandardCharsets.UTF_8.toString())

        // Act
        composeTestRule.setContent {
            ExchangeRateScreen(
                state = mockState,
                navController = rememberNavController(),
                encodedCountryString = encodedCountry
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Sierra Leone").assertExists()
        composeTestRule.onNodeWithText("Currency: Sierra Leonean leone (Le)").isDisplayed()
        composeTestRule.onNodeWithText("Exchange rate to USD: 4.4E-5").isDisplayed()
    }

    @Test
    fun testExchangeRateScreen_errorState() {
        // Arrange
        val mockState = ExchangeRateState(
            isLoading = false,
            error = "An error occurred"
        )
        val mockCountry = Country(
            name = "Sierra Leone",
            flagUrl = "https://flagcdn.com/w320/sl.png",
            currencies = mapOf("SLL" to Currency(name = "Leone", symbol = "Le"))
        )
        val encodedCountry = URLEncoder.encode(Json.encodeToString(mockCountry), StandardCharsets.UTF_8.toString())

        // Act
        composeTestRule.setContent {
            ExchangeRateScreen(
                state = mockState,
                navController = rememberNavController(),
                encodedCountryString = encodedCountry
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Sierra Leone").assertExists()
        composeTestRule.onNodeWithText("An error occurred").assertExists()
    }
}