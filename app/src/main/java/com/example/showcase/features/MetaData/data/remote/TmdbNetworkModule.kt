package com.example.showcase.features.MetaData.data.remote


import android.util.Log
import com.example.showcase.BuildConfig
import com.example.showcase.features.MetaData.data.remote.api.TmdbApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType

object TmdbNetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor =
        Interceptor { chain ->
            Log.d(
                "TMDB_KEY",
                BuildConfig.TMDB_API_KEY
            )

            val request =
                chain.request()
                    .newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${BuildConfig.TMDB_API_KEY}"
                    )
                    .addHeader(
                        "accept",
                        "application/json"
                    )
                    .build()

            chain.proceed(request)
        }

    val tmdbApi: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(TmdbApi::class.java)
    }

    private val client =
        OkHttpClient.Builder()

            .addInterceptor(authInterceptor)

            .addInterceptor(logger)

            .build()
}