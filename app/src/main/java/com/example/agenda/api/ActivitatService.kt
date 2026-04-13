package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivitatService {
    @GET("activitats/activitats")
    suspend fun llistaItems(): Response<List<ActivitatResponseDto>>

    @POST("activitats/activitat")
    suspend fun crearActivitat(
        @Body activitat: ActivitatRequestDto
    ): Response<ActivitatResponseDto>

    @GET("activitats/activitats/{idUsuari}")
    suspend fun llistaActivitatsByUsuari(
        @Path("idUsuari") idUsuari: Long
    ): Response<List<ActivitatResponseDto>>
}