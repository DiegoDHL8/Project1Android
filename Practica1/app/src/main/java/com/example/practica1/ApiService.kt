package com.example.practica1

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("0.2/languages")
    suspend fun getLanguage():Response<List<Language>>

    @Headers("Authorization: Bearer 35e604cdcf10b45116ccf3c36a2b77c9") //Encabezado que lleva la API_KEY
    @FormUrlEncoded
    @POST("/0.2/detect")
    suspend fun getTextLanguage(@Field("q")text:String):Response<DetectionResponse>
}