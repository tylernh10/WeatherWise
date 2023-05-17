package com.example.weatherwise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherwise.api.GooglePlacesResponse
import com.example.weatherwise.api.VisualCrossingResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

private const val TAG = "WeatherViewModel"


class WeatherViewModel : ViewModel() {
    private val weatherRepository = WeatherRepository()
    private val googlePlacesRepository = GooglePlacesRepository()

    private val _weatherResponse: MutableStateFlow<VisualCrossingResponse> =
        MutableStateFlow(VisualCrossingResponse())
    val weatherResponse: StateFlow<VisualCrossingResponse>
        get() = _weatherResponse.asStateFlow()

    private val _placesResponse: MutableStateFlow<GooglePlacesResponse> =
        MutableStateFlow(GooglePlacesResponse())
    val placesResponse: StateFlow<GooglePlacesResponse>
        get() = _placesResponse.asStateFlow()

    // type of place; 0 is park, 1 is museum, 2 is restaurant
    private var type = 0

    init {
        viewModelScope.launch {
            // TODO: cache response in sharedpreferences
            getWeather("06269")
        }
    }

    suspend fun getWeather(zipcode: String): Boolean {
        val regex = Regex("^\\d{5}$")
        if (!regex.matches(zipcode)) return false

        if (zipcode == weatherResponse.value.zipcode) return true

        return try {
            val res = weatherRepository.getWeather(zipcode)
            Log.i(TAG, "$res")
            _weatherResponse.value = res

            // make a call to places api once successfully retrieved weather info
            getPlaces(weatherResponse.value.latitude, weatherResponse.value.longitude, type, true)
            true
        } catch (e: Exception) {
            Log.d(TAG, "visual crossing API error")
            false
        }
    }

    suspend fun clickButton(buttonType: Int) {
        getPlaces(_weatherResponse.value.latitude, _weatherResponse.value.longitude, buttonType)
    }

    suspend fun getPlaces(latitude: Float, longitude: Float, newType: Int, newCall: Boolean = false): Boolean {
        if (type == newType && !newCall) {
            Log.d(TAG, "places before: ${_placesResponse.value}")
            val currentVal = _placesResponse.value
            val shuffledList = currentVal.results.shuffled()
            val updatedValue = currentVal.copy(results = shuffledList)
            _placesResponse.value = updatedValue
            Log.d(TAG, "places after: ${_placesResponse.value}")
            return true
        }

        type = newType

        return try {
            val strType = if (type == 0) {
                "park"
            } else if (type == 1) {
                "museum"
            } else {
                "restaurant"
            }

            val res = googlePlacesRepository.getPlaces(latitude, longitude, strType)
            Log.i(TAG, "$res")
            _placesResponse.value = res
            true
        } catch (e: Exception) {
            Log.d(TAG, "google places API error: e")
            false
        }
    }
}