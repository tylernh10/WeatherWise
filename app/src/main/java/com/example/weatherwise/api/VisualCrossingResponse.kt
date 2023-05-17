package com.example.weatherwise.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VisualCrossingResponse(
    @Json(name = "address") var zipcode: String = "--",
    var description: String = "No description found",
    var days: List<Day> = emptyList(),
    val latitude: Float = 0F,
    val longitude: Float = 0F
)