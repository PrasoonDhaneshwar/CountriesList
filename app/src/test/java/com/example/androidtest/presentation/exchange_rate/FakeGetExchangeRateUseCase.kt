import com.example.androidtest.common.Resource
import com.example.androidtest.countries.model.Currency
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.ExchangeRate
import com.example.androidtest.domain.use_case.get_exchange_rate.GetExchangeRateUseCase
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetExchangeRateUseCase : GetExchangeRateUseCase(mockk()) {
    private var shouldReturnError = false
    private var exchangeRateResponse: ExchangeRate? = null

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setExchangeRateResponse(response: ExchangeRate) {
        exchangeRateResponse = response
    }

    override fun invoke(currency: String): Flow<Resource<ExchangeRate>> = flow {
        emit(Resource.Loading())  // Emit loading state
        // Introduce a delay to simulate async behavior (this is important for testing)
        delay(100)

        emit(Resource.Loading()) // Emit loading state again if you want three emissions

        delay(100) // Additional delay for the next state
        if (shouldReturnError) {
            emit(Resource.Error("An error occurred")) // Emit error
        } else {
            emit(Resource.Success(exchangeRateResponse ?: ExchangeRate(base = currency, rates = emptyMap())))
        }
    }
}

val countries = listOf(
    Country(
        name = "Country1",
        currencies = mapOf("USD" to Currency("US Dollar", "$")),
        flagUrl = "http://example.com/flag1.png"
    ),
    Country(
        name = "Country2",
        currencies = mapOf("EUR" to Currency("Euro", "€")),
        flagUrl = "http://example.com/flag2.png"
    )
)