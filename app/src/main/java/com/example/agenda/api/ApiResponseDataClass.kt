package com.example.agenda.api

import com.google.gson.annotations.SerializedName

data class SalaResponseDto(
    val nom: String,
    val ubicacio: PisoSala,
    val descripcio: String,
    val activa: Boolean,
    val dataCreacio: String,
    val dataModificacio: String,
    val id: Long
)

data class SalaRequestDto(
    val nom: String,
    val ubicacio: PisoSala,
    val descripcio: String,
    val activa: Boolean
)

enum class PisoSala {
    P0, P4, P5
}


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

data class UsuariResponseDto(
    val nom: String?,
    val email: String,
    val rol: String?,
    val actiu: Boolean?,
    val provider: String?,
    val providerId: String?
)