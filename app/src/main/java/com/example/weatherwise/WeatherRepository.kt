package com.example.weatherwise

import android.annotation.SuppressLint
import com.example.weatherwise.api.VisualCrossingApi
import com.example.weatherwise.api.VisualCrossingResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class WeatherRepository {
    private val visualCrossingApi: VisualCrossingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://weather.visualcrossing.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        visualCrossingApi = retrofit.create<VisualCrossingApi>()
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun getWeather(zipcode: String): VisualCrossingResponse {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = formatter.format(currentDate)
        return visualCrossingApi.getWeather(zipcode, formattedDate.toString())
    }
}