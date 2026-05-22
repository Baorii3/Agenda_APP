package com.example.agenda.validators

import com.example.agenda.api.ActivitatRequestDto
import com.example.agenda.api.ActivitatResponseDto
import java.time.LocalDate
import java.time.LocalTime
import java.time.Duration

class ActivitatValidator {
    companion object {

        fun validar(
            actRequest: ActivitatRequestDto,
            activitatsExistentes: List<ActivitatResponseDto> = emptyList(),
            idPropio: Long? = null
        ): String? {
            if (actRequest.idUsuari == null) {
                return "Los IDs de la sala y el usuario son obligatorios."
            }

            val horaInici = try {
                LocalTime.parse(actRequest.horaInici)
            } catch (e: Exception) {
                return "La hora de inicio no tiene un formato válido (HH:mm)."
            }

            val horaFi = try {
                LocalTime.parse(actRequest.horaFi)
            } catch (e: Exception) {
                return "La hora de fin no tiene un formato válido (HH:mm)."
            }

            val data = try {
                LocalDate.parse(actRequest.data)
            } catch (e: Exception) {
                return "La fecha no tiene un formato válido (yyyy-MM-dd)."
            }

            if (horaInici.minute % 15 != 0) {
                return "La hora de inicio debe empezar en un cuarto de hora."
            }

            if (horaFi.minute % 15 != 0) {
                return "La hora de fin debe empezar en un cuarto de hora."
            }

            if (!horaFi.isAfter(horaInici)) {
                return "La hora de inicio debe ser anterior a la hora de fin."
            }

            val duracionMinutos = Duration.between(horaInici, horaFi).toMinutes()
            if (duracionMinutos > 120) {
                return "La actividad no puede durar más de 2 horas."
            }

            if (data.isBefore(LocalDate.now())) {
                return "La fecha de la actividad no puede ser anterior a la fecha actual."
            }

            if (data.isEqual(LocalDate.now()) && horaInici.isBefore(LocalTime.now())) {
                return "La hora de inicio de la actividad no puede ser anterior a la hora actual."
            }

            val existeConflicto = activitatsExistentes.any { existente ->
                if (idPropio != null && existente.idActivitat == idPropio) {
                    return@any false
                }

                if (existente.idSala != actRequest.idSala || existente.data != actRequest.data) {
                    return@any false
                }

                val existeHoraInici = try {
                    LocalTime.parse(existente.horaInici)
                } catch (e: Exception) {
                    return@any false
                }

                val existeHoraFi = try {
                    LocalTime.parse(existente.horaFi)
                } catch (e: Exception) {
                    return@any false
                }

                horaInici < existeHoraFi && horaFi > existeHoraInici
            }

            if (existeConflicto) {
                return "La sala ya está ocupada en ese horario."
            }

            return null
        }
    }
}

