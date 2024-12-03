package android.template.data.di

import android.template.data.api.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    fun createWeatherApiService(retrofit: Retrofit): WeatherService =
        retrofit
            .create<WeatherService>()
}
