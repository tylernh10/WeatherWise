package com.example.weatherwise.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Place(
    @Json(name = "formatted_address") val address: String,
    val name: String,
    val rating: Float,
    val types: List<String>
)