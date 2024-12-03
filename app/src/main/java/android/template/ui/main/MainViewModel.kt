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

package android.template.ui.main

import android.template.data.repository.WeatherRepository
import android.template.data.api.model.WeatherResponse
import android.template.ui.main.MainUiState.Error
import android.template.ui.main.MainUiState.Loading
import android.template.ui.main.MainUiState.Success
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    companion object {
        const val DEFAULT_PLACE = "BANGKOK"
    }

    private var place = MutableStateFlow(DEFAULT_PLACE)

    val uiState: StateFlow<MainUiState> = place
        .flatMapLatest { place ->
            weatherRepository.cityWeather(place)
        }
        .map<WeatherResponse, MainUiState>(::Success)
        .catch { throwable ->
            emit(Error(throwable))
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, Loading)

    fun updateQuery(city: String) {
        viewModelScope.launch {
            place.emit(city)
        }
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
    data class Success(val data: WeatherResponse) : MainUiState
}
