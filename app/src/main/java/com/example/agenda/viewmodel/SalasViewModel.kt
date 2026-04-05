package com.example.agenda
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agenda.api.Api
import com.example.agenda.api.SalaResponseDto
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.ActivitatResponseDto
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

}