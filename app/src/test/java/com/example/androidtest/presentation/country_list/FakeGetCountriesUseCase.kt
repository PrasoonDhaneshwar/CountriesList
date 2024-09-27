package com.example.androidtest.presentation.country_list

import com.example.androidtest.common.Resource
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.use_case.get_countries.GetCountriesUseCase
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetCountriesUseCase : GetCountriesUseCase(mockk()) {
    private var shouldReturnError = false
    private var countriesResponse: List<Country> = emptyList()

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setCountriesResponse(response: List<Country>) {
        countriesResponse = response
    }

    override fun invoke(): Flow<Resource<List<Country>>> = flow {
        emit(Resource.Loading()) // Emit loading state
        delay(100) // Simulate delay

        emit(Resource.Loading()) // Emit loading state again if you want three emissions

        delay(100) // Additional delay for the next state
        if (shouldReturnError) {
            emit(Resource.Error("An error occurred"))
        } else {
            emit(Resource.Success(countriesResponse))
        }
    }
}