import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import com.example.androidtest.presentation.country_list.CountryListItem
import com.example.androidtest.presentation.ui.theme.AndroidTheme
import org.junit.Rule
import org.junit.Test

class CountryListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun countryListItem_displaysCountryData() {
        // Create a sample country object
        val sampleCountry = Country(
            name = "France",
            currencies = mapOf("EUR" to Currency(name = "Euro", symbol = "€")),
            flagUrl = "https://flagcdn.com/w320/fr.png"
        )

        // Set up the composable to test
        composeTestRule.setContent {
            AndroidTheme {
                CountryListItem(country = sampleCountry, navController = rememberNavController())
            }
        }

        // Verify that the country name is displayed
        composeTestRule.onNodeWithText("France").assertIsDisplayed()

        // Verify that the currency name and symbol are displayed
        composeTestRule.onNodeWithText("Euro (€)").assertIsDisplayed()
    }

}