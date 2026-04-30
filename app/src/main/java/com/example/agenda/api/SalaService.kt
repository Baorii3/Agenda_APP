package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface SalaService {

    @GET("salas")
    suspend fun llistaSala(): Response<List<SalaResponseDto>>

    @POST("salas")
    suspend fun crearSala(
        @Body sala: SalaRequestDto
    ): Response<SalaResponseDto>

    @DELETE("salas/{id}")
    suspend fun eliminarSala(
        @Path("id") id: Long,
    ): Response<Void>
}