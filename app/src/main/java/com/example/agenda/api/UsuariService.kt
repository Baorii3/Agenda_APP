package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface UsuariService {
    @POST("usuaris/token")
    suspend fun crearUsuario(
        @Header("Authorization") authHeader: String
    ): Response<UsuariResponseDto>
}