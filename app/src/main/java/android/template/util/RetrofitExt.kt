package android.template.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit

fun retrofit(init: Retrofit.Builder.() -> Unit): Retrofit {
    val retrofit = Retrofit.Builder()
    retrofit.init()
    return retrofit.build()
}

fun Retrofit.Builder.okHttpClient(init: OkHttpClient.Builder.() -> Unit): Retrofit.Builder {
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.init()
    val okHttpClient = okHttpClientBuilder.build()
    client(okHttpClient)
    return this
}
