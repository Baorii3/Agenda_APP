package com.example.agenda.api

import com.google.gson.annotations.SerializedName

data class SalaResponseDto(
    val nom: String,
    val tipus: String,
    val ubicacio: String,
    val descripcio: String,
    val activa: Boolean,
    val dataCreacio: String,
    val dataModificacio: String,
    val id: Long
)

data class ActivitatResponseDto(

    @SerializedName("id_activitat")
    val idActivitat: Long,
    @SerializedName("id_sala")
    val idSala: Long,
    @SerializedName("google_id")
    val googleId: String,
    val titol: String,
    val resum: String,
    val descripcio: String,
    val data: String,
    val horaInici: String,
    val horaFi: String,
    val estat: Estat,
    val visible: Boolean,
    val dataCreacio: String,
    val dataModificacio: String
)

enum class Estat {
    programada,
    cancelada
}