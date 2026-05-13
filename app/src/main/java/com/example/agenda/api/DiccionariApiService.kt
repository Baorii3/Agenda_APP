package com.example.agenda.api

import retrofit2.http.GET

interface DiccionariApiService {
    @GET("pisos")
    suspend fun getPisos(): List<String>
}