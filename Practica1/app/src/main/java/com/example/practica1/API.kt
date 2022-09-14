package com.example.practica1

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    private const val  BASE_URL = "https://ws.detectlanguage.com/"
    val retrofitService: ApiService by lazy{
        getRetrofit().create(ApiService::class.java)
    }
    private fun getRetrofit():Retrofit{ //devuelve un objeto retrofit
        val logging = HttpLoggingInterceptor() //devuelve un log en cada llamada.
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
}