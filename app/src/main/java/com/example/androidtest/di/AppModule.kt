package com.example.androidtest.di

import com.example.androidtest.common.Constants.BASE_URL
import com.example.androidtest.common.Constants.EXCHANGE_RATE_URL
import com.example.androidtest.countries.CountriesApi
import com.example.androidtest.data.repository.CountryRepositoryImpl
import com.example.androidtest.data.repository.ExchangeRateRepositoryImpl
import com.example.androidtest.domain.repository.CountryRepository
import com.example.androidtest.domain.repository.ExchangeRateRepository
import com.prasoon.exchangerate.ExchangeRateApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @OptIn(ExperimentalSerializationApi::class)
    val json by lazy {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            // explicitNulls, defines whether null property
            // values should be included in the serialized JSON string.
            explicitNulls = false

            // If API returns null or unknown values for Enums, we can use default constructor parameter to override it
            // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/json.md#coercing-input-values
            coerceInputValues = true
        }
    }

    private val contentType = "application/json; charset=utf-8".toMediaType()

    @Provides
    @Singleton
    fun provideCountriesApi(): CountriesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CountriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCountryRepository(api: CountriesApi): CountryRepository {
        return CountryRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideExchangeRateApi(): ExchangeRateApi {
        return Retrofit.Builder()
            .baseUrl(EXCHANGE_RATE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ExchangeRateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRateRepository(countryExchangeApi: ExchangeRateApi): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(countryExchangeApi)
    }
}