package com.example.weatherwise.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GooglePlacesResponse(
    var results: List<Place> = emptyList()
)