package com.example.weatherwise.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.util.*

private const val API_KEY = "API_KEY_HERE"

interface GooglePlacesApi {
    @GET("/maps/api/place/textsearch/json") // ?location=41%2C-72&query=restaurant&radius=100&key=$API_KEY")
    suspend fun getPlaces(
        @Query("location") location: String,
        @Query("type") type: String,
        @Query("key") key: String = API_KEY
    ): GooglePlacesResponse
}
