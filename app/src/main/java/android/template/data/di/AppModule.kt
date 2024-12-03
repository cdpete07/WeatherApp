package android.template.data.di

import android.template.BuildConfig
import android.template.util.retrofit
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
            explicitNulls = false
            coerceInputValues = true
        }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor()
            .apply {
                if (BuildConfig.DEBUG) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            },
    ).build()

    @Provides
    @Singleton
    fun provideRetrofit(json: Json) =
        retrofit {
            baseUrl(BuildConfig.BASEURL)
            callFactory { okHttpCallFactory().newCall(it) }
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
}