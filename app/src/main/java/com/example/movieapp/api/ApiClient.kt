package com.example.movieapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "17046dd06bc2247e0e2be21519d28fb4"
const val IMAGE_BASE_URL_185 = "https://image.tmdb.org/t/p/w185"
const val IMAGE_BASE_URL_500 = "https://image.tmdb.org/t/p/w500"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

object ApiClient {
    fun getClient(): ApiInterface {
        val interceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter(
                    "api_key",
                    API_KEY
                ).build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}