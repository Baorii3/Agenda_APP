package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.GET

interface ActivitatService {
    @GET("activitats/activitats")
    suspend fun llistaItems(): Response<List<ActivitatResponseDto>>

}