package com.example.androidtest.domain.use_case.get_exchange_rate

import com.example.androidtest.common.Resource
import com.example.androidtest.domain.model.ExchangeRate
import com.example.androidtest.domain.model.toExchangeRateUI
import com.example.androidtest.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val repository: ExchangeRateRepository
) {
    operator fun invoke(currency: String): Flow<Resource<ExchangeRate>> = flow {
        try {
            emit(Resource.Loading<ExchangeRate>())
            val exchangeRateUI = repository.getCountryExchangeRate(currency).toExchangeRateUI()
            emit(Resource.Success<ExchangeRate>(exchangeRateUI))
        } catch (e: HttpException) {
            emit(Resource.Error<ExchangeRate>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<ExchangeRate>("Couldn't reach server. Check your internet connection."))
        }
    }
}