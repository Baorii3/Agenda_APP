package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActivitatService {
    @GET("activitats")
    suspend fun llistaItems(): Response<List<ActivitatResponseDto>>

    @POST("activitats")
    suspend fun crearActivitat(
        @Body activitat: ActivitatRequestDto
    ): Response<ActivitatResponseDto>

    @GET("activitats/{idUsuari}")
    suspend fun llistaActivitatsByUsuari(
        @Path("idUsuari") idUsuari: Long
    ): Response<List<ActivitatResponseDto>>

    @DELETE("activitats/{id}")
    suspend fun deleteByID(
        @Path("id") id: Long
    ): Response<Unit>

    @PUT("activitats/{id}")
    suspend fun editarActivitat(
        @Path("id") id: Long,
        @Body activitat: ActivitatRequestDto
    ): Response<ActivitatResponseDto>
}