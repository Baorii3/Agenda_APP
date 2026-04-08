package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface SalaService {

    @GET("sala/salas")
    suspend fun llistaSala(): Response<List<SalaResponseDto>>

    @POST("sala/salas")
    suspend fun crearSala(
        @Body sala: SalaRequestDto
    ): Response<SalaResponseDto>
}