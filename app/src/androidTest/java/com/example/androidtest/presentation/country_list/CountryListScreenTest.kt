import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import com.example.androidtest.presentation.country_list.CountriesListState
import com.example.androidtest.presentation.country_list.CountryListScreen
import com.example.androidtest.presentation.ui.theme.AndroidTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCountry = Country(
        name = "Germany",
        currencies = mapOf("EUR" to Currency(name = "Euro", symbol = "€")),
        flagUrl = "https://flagcdn.com/w320/de.png"
    )

    @Test
    fun countryListScreen_displaysCountries() {
        val countries = listOf(sampleCountry)

        // Set up the composable to test
        composeTestRule.setContent {
            AndroidTheme {
                CountryListScreen(
                    state = CountriesListState(countries = countries),
                    navController = rememberNavController() // Mock NavController for testing
                )
            }
        }

        // Verify that the country name is displayed
        composeTestRule.onNodeWithText("Germany").assertIsDisplayed()

        // Verify that the currency is displayed
        composeTestRule.onNodeWithText("Euro (€)").assertIsDisplayed()
    }

    @Test
    fun countryListScreen_showsLoadingIndicator() {
        // Set up the composable with the loading state
        composeTestRule.setContent {
            AndroidTheme {
                CountryListScreen(
                    state = CountriesListState(isLoading = true),
                    navController = rememberNavController()
                )
            }
        }

        // Verify that the loading indicator is displayed
        composeTestRule.onNode(isRoot()).onChildAt(1).assertIsDisplayed()
        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }

    @Test
    fun countryListScreen_displaysErrorMessage() {
        val errorMessage = "Failed to load countries"

        // Set up the composable with an error state
        composeTestRule.setContent {
            AndroidTheme {
                CountryListScreen(
                    state = CountriesListState(error = errorMessage),
                    navController = rememberNavController()
                )
            }
        }

        // Verify that the error message is displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}