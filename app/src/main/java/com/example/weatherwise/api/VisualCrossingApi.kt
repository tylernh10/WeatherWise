package com.example.weatherwise.api

import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate
import java.util.*

private const val API_KEY = "API_KEY_HERE"

interface VisualCrossingApi {
    @GET("VisualCrossingWebServices/rest/services/timeline/{location}/{date}?key=$API_KEY")
    suspend fun getWeather(
        @Path("location") zipcode: String,
        @Path("date") date: String
    ): VisualCrossingResponse
}
