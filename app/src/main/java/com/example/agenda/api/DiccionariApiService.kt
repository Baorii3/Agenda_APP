package com.example.agenda.api

import retrofit2.http.GET

interface DiccionariApiService {
    @GET("dictionary/pisos-sala")
    suspend fun getPisos(): List<String>
}