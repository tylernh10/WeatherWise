package com.example.weatherwise.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Day(
    val temp: Float,
    val precipprob: Float,
    val humidity: Float,
    val conditions: String
)