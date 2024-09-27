package com.example.androidtest.domain.use_case.get_countries

import com.example.androidtest.common.Resource
import com.example.androidtest.domain.model.Country
import com.example.androidtest.domain.model.toCountryUI
import com.example.androidtest.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

open class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    open operator fun invoke(): Flow<Resource<List<Country>>> = flow {
        try {
            emit(Resource.Loading<List<Country>>())
            val countries = repository.getCountries().map { it.toCountryUI() }
            emit(Resource.Success<List<Country>>(countries))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Country>>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Country>>("Couldn't reach server. Check your internet connection."))
        }
    }
}