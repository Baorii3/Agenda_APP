package com.example.agenda.api

import android.R
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class SalaResponseDto(
    @SerializedName("id_sala")
    val id: Long,
    val nom: String,
    val ubicacio: String,
    val descripcio: String,
    val activa: Boolean,
    val colorHex: String
)

data class SalaRequestDto(
    val nom: String,
    val ubicacio: String,
    val descripcio: String,
)



data class ActivitatRequestDto(
    @SerializedName("id_sala")
    val idSala: Long,
    @SerializedName("id_usuari")
    val idUsuari: Long,
    val titol: String,
    val descripcio: String,
    val data: String,
    val horaInici: String,
    val horaFi: String
)

data class ActivitatResponseDto(

    @SerializedName("id_activitat")
    val idActivitat: Long,
    @SerializedName("id_sala")
    val idSala: Long,
    @SerializedName("nom_sala")
    val nomSala: String,
    @SerializedName("id_usuari")
    val idUsuari: Long,
    @SerializedName("nom_usuari")
    val nomUsuari: String,
    val titol: String,
    val descripcio: String,
    val data: String,
    val horaInici: String,
    val horaFi: String,
    val activa: Boolean
)
data class UsuariResponseDto(
    @SerializedName("id_usuari")
    val idUsuari: Long,
    val nom: String,
    val email: String,
    val rol: String,
    val permisos: List<PermisoDto>,
    val picture: String
)
data class PermisoDto(
    val recurso: String,
    val valor: Long
)

data class DispositiuResponseDto(
    @SerializedName("id_dispositiu")
    val id: Long,
    val nom: String,
    val mac: String,
    val ip: String,
    val tipus: String,
    val actiu: Boolean,
    val dataCreacio: String,
    val heartbeat: String?
)
