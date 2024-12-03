Architecture starter template (single module)
==================

## Features

* Room Database
* Hilt
* ViewModel, read+write
* UI in Compose, list + write (Material3)
* Navigation
* Repository and data source
* Kotlin Coroutines and Flow
* Unit tests
* UI tests using fake data with Hilt

# Android Weather App

## Description
This is an Android project that integrates with the [OpenWeatherMap API](https://api.openweathermap.org) to provide weather information.

Before running the app, you need to configure some essential values in the `build.gradle.kts` file.

---

## Prerequisites

1. **Obtain API Credentials**
    - Go to [OpenWeatherMap API](https://api.openweathermap.org) and sign up.
    - Get your `API_KEY`.

2. **Add the `BASEURL`**
    - The base URL for OpenWeatherMap API is:
      ```
      https://api.openweathermap.org/
      ```

---

## Configuration

To run the app, you need to add the `BASEURL` and `API_KEY` in your `build.gradle` file using the `buildConfigField` method.

### Steps:

1. Open the `app/build.gradle` file.
2. Add the following lines under the `android` block, inside the `buildTypes` section:

```gradle
android {
    ...
    buildTypes {
        debug {
            buildConfigField "String", "BASEURL", "\"https://api.openweathermap.org/\""
            buildConfigField "String", "API_KEY", "\"your_api_key_here\""
        }
        release {
            buildConfigField "String", "BASEURL", "\"https://api.openweathermap.org/\""
            buildConfigField "String", "API_KEY", "\"your_api_key_here\""
            ...
        }
    }
}
