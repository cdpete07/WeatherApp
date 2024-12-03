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

import android.template.R
import android.template.data.api.model.WeatherResponse
import android.template.ui.theme.MyApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is MainUiState.Success, is MainUiState.Error -> {
            MainScreen(
                modifier = modifier,
                uiState = uiState,
                onSearchLocationClick = viewModel::updateQuery
            )
        }

        is MainUiState.Loading -> {
            ContentLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onSearchLocationClick: (String) -> Unit
) {
    var cityName by remember { mutableStateOf(MainViewModel.DEFAULT_PLACE) }
    var isFieldError by remember { mutableStateOf(false) }
    val (focusedRequester) = remember { FocusRequester.createRefs() }


    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_title),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            )
        }
    ) { insets ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(insets)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusedRequester),
                value = cityName,
                onValueChange = { newValue -> cityName = newValue },
                maxLines = 1,
                placeholder = {
                    Text(
                        text = stringResource(R.string.place_placeholder),
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusedRequester.requestFocus()
                        onSearchLocationClick(cityName)
                    }
                ),
                isError = isFieldError,
                supportingText = {
                    if (isFieldError) {
                        Text(
                            text = stringResource(R.string.place_helper_text),
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            color = Color.Red
                        )
                    }
                }
            )

            Content(uiState)

            FloatingActionButton(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                onClick = {
                    if (cityName.isNotEmpty()) {
                        onSearchLocationClick(cityName)
                    }
                    isFieldError = cityName.isEmpty()
                }
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContentLoading() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_title),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            )
        }) { insets ->
        Box(
            modifier = Modifier
                .padding(insets)
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
internal fun BoxScope.SuccessContent(weather: WeatherResponse) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .align(Alignment.Center)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val temperature = weather.main.temp.roundToInt().toString()
        val feelsLike = weather.main.feels_like.roundToInt()
        val main = weather.weather.firstOrNull()?.main.orEmpty()
        val description = weather.weather.firstOrNull()?.description.orEmpty()

        Text(
            text = main,
            fontWeight = FontWeight.Thin,
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = temperature,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 100.sp,
                    textAlign = TextAlign.Center
                )
                when {
                    weather.main.temp >= 25 -> {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(R.drawable.ic_sun),
                            contentDescription = "Sunny"
                        )
                    }

                    else -> {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = painterResource(R.drawable.ic_clouds),
                            contentDescription = "Clouds"
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.feels_like_text, feelsLike),
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp
            )

        }

        Text(
            text = description,
            fontWeight = FontWeight.Thin,
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp
        )
    }
}

@Composable
internal fun Content(uiState: MainUiState) {
    Box {
        when (uiState) {
            is MainUiState.Success -> {
                SuccessContent(
                    uiState.data
                )
            }

            is MainUiState.Error -> {
                ErrorContent(uiState.throwable)
            }

            else -> {
                //NO OPS
            }
        }
    }
}

@Composable
internal fun ErrorContent(throwable: Throwable) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            throwable is retrofit2.HttpException -> {
                when (throwable.code()) {
                    in 300..399 -> {
                        Text(
                            text = stringResource(R.string.internet_300_error_text),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    in 400..499 -> {
                        if (throwable.code() == 404) {
                            Text(
                                text = stringResource(R.string.internet_404_error_text),
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.internet_400_error_text),
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = stringResource(R.string.internet_500_error_text),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            else -> {
                Text(
                    text = stringResource(R.string.general_error_text),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}
