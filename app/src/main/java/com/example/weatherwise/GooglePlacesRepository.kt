package com.example.weatherwise

import com.example.weatherwise.api.GooglePlacesApi
import com.example.weatherwise.api.GooglePlacesResponse
import com.example.weatherwise.api.VisualCrossingResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class GooglePlacesRepository {
    private val googlePlacesApi: GooglePlacesApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        googlePlacesApi = retrofit.create<GooglePlacesApi>()
    }

    suspend fun getPlaces(latitude: Float, longitude: Float, type: String): GooglePlacesResponse {
        return googlePlacesApi.getPlaces("$latitude,$longitude", type)
    }
}