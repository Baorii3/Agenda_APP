package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UsuariService {
    @POST("usuaris/token")
    suspend fun crearUsuario(
        @Header("Authorization") authHeader: String
    ): Response<UsuariResponseDto>

    @GET("usuaris/usuaris/profes")
    suspend fun getProfesores(
    ): Response<List<UsuariResponseDto>>
}