package android.template.data.api.service

import android.template.BuildConfig
import android.template.data.api.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val METRIC_UNIT = "metric"
interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") appId: String = BuildConfig.API_KEY,
        @Query("units") units: String = METRIC_UNIT
    ): WeatherResponse
}