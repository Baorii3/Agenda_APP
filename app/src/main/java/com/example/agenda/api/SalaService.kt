package com.example.agenda.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface SalaService {

    @GET("sala/salas")
    suspend fun llistaSala(): Response<List<SalaResponseDto>>

}