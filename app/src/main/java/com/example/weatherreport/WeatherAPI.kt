package com.example.weatherreport

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(
    val location: Locations,
    val current: Current,
)
data class Locations(
    val name: String,
    val country: String,
)
data class Current(
    val condition: Condition,
    val temp_c: Float,
    val humidity: Int,
    val feelslike_c: Float,
    val wind_kph: Float,
    val wind_dir: String,
    val uv: Float,
    val vis_km: Float,
)
data class Condition(
    val text: String,
)

// Fetch request via Retrofit interface
interface WeatherApi {
    // Endpoint path
    @GET("current.json")
    // Coroutine query call
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("aqi") includeAqi: String = "yes"
    ): WeatherResponse
}

// Makes an API call by querying the user's latitude and longitude
fun getWeather(coroutineScope: CoroutineScope,
               locationHelper: LocationHelper,
               onResult: (List<Pair<String, String>>?) -> Unit) {

    // Call and receive the data as JSON
    val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    // Get location values from location services
    locationHelper.getCurrentLocation { location ->
        if (location != null) {
            val lat = location.latitude
            val lon = location.longitude

            // Coroutine builder call
            coroutineScope.launch {
                try {
                    // Calls the coroutine query with the APIKey and location data
                    val response = weatherApi.getCurrentWeather(
                        "c69e4764d5dd408284911145211104",
                        "${lat},${lon}")
                    onResult(processResponse(response))
                } catch (e: Exception) {
                    // Error
                    println(e)
                    onResult(null)
                }
            }
        } else {
            // Error
            println("Location Services ERROR")
        }
    }
}

// Assign values from the API to the List of String
fun processResponse(response: WeatherResponse): List<Pair<String, String>> {
    return listOf(
        Pair(response.location.name, ""),
        Pair(response.location.country, ""),
        Pair(response.current.condition.text, ""),
        Pair("${response.current.temp_c}", " °C"),
        Pair("Humidity: ", "${response.current.humidity}%"),
        Pair("Feels Like: ", "${response.current.feelslike_c} °C"),
        Pair("Wind Speed: ", "${response.current.wind_kph} kph"),
        Pair("Wind Direction: ", response.current.wind_dir),
        Pair("UV Index: ", "${response.current.uv}"),
        Pair("Visibility: ", "${response.current.vis_km} km")
    )
}