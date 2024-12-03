/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.data.repository

import android.template.data.api.model.WeatherResponse
import android.template.data.api.service.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface WeatherRepository {
    suspend fun cityWeather(cityName: String): Flow<WeatherResponse>
}

class DefaultWeatherRepository @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {

    override suspend fun cityWeather(cityName: String): Flow<WeatherResponse> {
        return flowOf(weatherService.getWeather(cityName)).flowOn(Dispatchers.IO)
    }
}
