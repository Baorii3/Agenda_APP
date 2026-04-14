package com.example.agenda
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agenda.api.Api
import com.example.agenda.api.SalaResponseDto
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.ActivitatRequestDto
import com.example.agenda.api.ActivitatResponseDto
import com.example.agenda.api.SalaRequestDto
import kotlinx.coroutines.launch

class SalaViewModel : ViewModel() {
    private val _salas = MutableLiveData<MutableList<SalaResponseDto>>()
    val salas: MutableLiveData<MutableList<SalaResponseDto>> = _salas

    private val _activitats = MutableLiveData<MutableList<ActivitatResponseDto>>()
    val activitats: MutableLiveData<MutableList<ActivitatResponseDto>> = _activitats

    fun cargarSalasApi() {
        viewModelScope.launch {
            try {
                val response = Api.getSalaService().llistaSala()
                if (response.isSuccessful) {
                    val items = response.body()
                    _salas.postValue(items?.toMutableList() ?: mutableListOf())
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Api", "Error de connexió", e)
            }
        }
    }

    fun findSalaById(id: Long): SalaResponseDto? {
        return _salas.value?.find { it.id == id }
    }

    fun cargarActivitatsForSala(salaId: Long) {
        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().llistaItems()
                if (response.isSuccessful) {
                    // deberia mejorar esto con una consulta a la API que filtre por salaId, pero bueno, de momento esto
                    val items = response.body()
                    val activitatsSala = items?.filter { it.idSala == salaId } ?: emptyList()
                    _activitats.postValue(activitatsSala.toMutableList())
                    Log.d("SalaViewModel", "Activitats for salaId=$salaId: $activitatsSala")
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Api", "Error de connexió", e)
            }
        }
    }

    fun addSala(sala: SalaRequestDto) {
        viewModelScope.launch {
            try {
                val response = Api.getSalaService().crearSala(sala)
                if (response.isSuccessful) {
                    val nuevaSala = response.body()
                    if (nuevaSala != null) {
                        val salasActuales = _salas.value ?: mutableListOf()
                        salasActuales.add(nuevaSala)
                        _salas.postValue(salasActuales)
                    }
                } else {
                    Log.e("API", "Error HTTP al crear sala: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de conexión al crear sala", e)
            }
        }
    }

    fun crearActivitat(actRequest: ActivitatRequestDto) {
        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().crearActivitat(actRequest)
                if (response.isSuccessful) {
                    val nuevaActivitat = response.body()
                    if (nuevaActivitat != null) {
                        val activitatsActuales = _activitats.value ?: mutableListOf()
                        activitatsActuales.add(nuevaActivitat)
                        _activitats.postValue(activitatsActuales)
                    }
                } else {
                    Log.e("APICREATE", "Error HTTP al crear activitat: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("APICREATE", "Error de conexión al crear activitat", e)
            }
        }
    }
}