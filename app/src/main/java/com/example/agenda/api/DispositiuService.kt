package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.GET

interface DispositiuService {
    @GET("dispositius")
    suspend fun llistaDispositius() : Response<List<DispositiuResponseDto>>

}