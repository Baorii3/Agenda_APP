package com.example.agenda.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agenda.api.Api
import com.example.agenda.api.SalaResponseDto
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.ActivitatRequestDto
import com.example.agenda.api.ActivitatResponseDto
import com.example.agenda.api.SalaRequestDto
import com.example.agenda.validators.ActivitatValidator
import kotlinx.coroutines.launch

class SalaViewModel : ViewModel() {
    private val _salas = MutableLiveData<MutableList<SalaResponseDto>>()
    val salas: MutableLiveData<MutableList<SalaResponseDto>> = _salas

    private val _activitats = MutableLiveData<MutableList<ActivitatResponseDto>>()
    val activitats: MutableLiveData<MutableList<ActivitatResponseDto>> = _activitats

    private val _activitatsPorDia = MutableLiveData<MutableList<ActivitatResponseDto>>()
    val activitatsPorDia: MutableLiveData<MutableList<ActivitatResponseDto>> = _activitatsPorDia

    fun cargarSalasApi(onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = Api.getSalaService().llistaSala()
                if (response.isSuccessful) {
                    val items = response.body()
                    _salas.postValue(items?.toMutableList() ?: mutableListOf())
                } else {
                    onMessage("Error HTTP al cargar salas: ${response.code()}")
                }
            } catch (_: Exception) {
                onMessage("Error de conexión al cargar salas")
            }
        }
    }

    fun findSalaById(id: Long): SalaResponseDto? {
        return _salas.value?.find { it.id == id }
    }

    fun cargarActivitatsForSala(salaId: Long, onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().llistaItems()
                if (response.isSuccessful) {
                    val items = response.body()
                    val activitatsSala = items?.filter { it.idSala == salaId } ?: emptyList()
                    _activitats.postValue(activitatsSala.toMutableList())
                } else {
                    onMessage("Error HTTP al cargar actividades: ${response.code()}")
                }
            } catch (_: Exception) {
                onMessage("Error de conexión al cargar actividades")
            }
        }
    }

    fun buscarActivitatsPorDia(dateIso: String, onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().llistaItems()
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    val filtradas = items.filter { activitat ->
                        val fecha = activitat.data
                        if (fecha.length >= 10) {
                            fecha.take(10) == dateIso
                        } else {
                            false
                        }
                    }
                    _activitatsPorDia.postValue(filtradas.toMutableList())
                } else {
                    onMessage("Error HTTP al filtrar actividades: ${response.code()}")
                }
            } catch (_: Exception) {
                onMessage("Error de conexión al filtrar actividades")
            }
        }
    }

    fun addSala(sala: SalaRequestDto, onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = Api.getSalaService().crearSala(sala)
                if (response.isSuccessful) {
                    val nuevaSala = response.body()
                    if (nuevaSala != null) {
                        val salasActuales = _salas.value ?: mutableListOf()
                        salasActuales.add(nuevaSala)
                        _salas.postValue(salasActuales)
                        onMessage("Sala creada correctamente")
                    }
                } else {
                    onMessage("Error HTTP al crear sala: ${response.code()}")
                }
            } catch (_: Exception) {
                onMessage("Error de conexión al crear sala")
            }
        }
    }

    fun crearActivitat(actRequest: ActivitatRequestDto, onMessage: (String) -> Unit = {}) {
        val errorValidacion = ActivitatValidator.validar(actRequest, _activitats.value ?: emptyList(), idPropio = null)
        if (errorValidacion != null) {
            onMessage(errorValidacion)
            return
        }

        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().crearActivitat(actRequest)
                if (response.isSuccessful) {
                    val nuevaActivitat = response.body()
                    if (nuevaActivitat != null) {
                        val activitatsActuales = _activitats.value ?: mutableListOf()
                        activitatsActuales.add(nuevaActivitat)
                        _activitats.postValue(activitatsActuales)
                        onMessage("Actividad creada correctamente")
                    }
                } else {
                    onMessage("Error HTTP al crear actividad: ${response.code()}")
                }
            } catch (_: Exception) {
                onMessage("Error de conexión al crear actividad")
            }
        }
    }

    fun deleteSalas(id: Long, onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            val response = Api.getSalaService().eliminarSala(id)
            if (response.isSuccessful) {
                val salasActuales = _salas.value ?: mutableListOf()
                salasActuales.removeIf { it.id == id }
                _salas.postValue(salasActuales)
                onMessage("Sala eliminada correctamente")
            } else {
                onMessage("Error HTTP al eliminar sala: ${response.code()}")
            }
        }
    }

    fun deleteActivitats(id: Long, onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            val response = Api.getActivitatService().deleteByID(id)
            if (response.isSuccessful) {
                val activitats = _activitats.value ?: mutableListOf()
                activitats.removeIf { it.idActivitat == id }
                _activitats.postValue(activitats)
                onMessage("Actividad eliminada correctamente")
            } else {
                onMessage("Error HTTP al eliminar actividad: ${response.code()}")
            }
        }
    }

    fun updateSala(id: Long, sala: SalaRequestDto, onMessage: (String) -> Unit = {}) {
        viewModelScope.launch {
            val response = Api.getSalaService().editarSala(id, sala)
            if (response.isSuccessful) {
                val index = _salas.value?.indexOfFirst { it.id == id }
                if (index != null && index != -1) {
                    val salasActuales = _salas.value ?: mutableListOf()
                    salasActuales[index] = response.body() ?: salasActuales[index]
                    _salas.postValue(salasActuales)
                    onMessage("Sala editada correctamente")
                }
            } else {
                onMessage("Error HTTP al editar sala: ${response.code()}")
            }
        }
    }

    fun updateActivitat(id: Long, activitat: ActivitatRequestDto, onMessage: (String) -> Unit = {}) {
        val errorValidacion = ActivitatValidator.validar(activitat, _activitats.value ?: emptyList(), idPropio = id)
        if (errorValidacion != null) {
            onMessage(errorValidacion)
            return
        }

        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().editarActivitat(id, activitat)
                if (response.isSuccessful) {
                    val activitatActualitzada = response.body()
                    if (activitatActualitzada != null) {
                        val activitatsActuals = _activitats.value ?: mutableListOf()
                        val index = activitatsActuals.indexOfFirst { it.idActivitat == id }
                        if (index != -1) {
                            activitatsActuals[index] = activitatActualitzada
                            _activitats.postValue(activitatsActuals)
                            onMessage("Actividad editada correctamente")
                        }
                    }
                } else {
                    onMessage("Error HTTP al editar actividad: ${response.code()}")
                }
            } catch (_: Exception) {
                onMessage("Error de conexión al editar actividad")
            }
        }
    }
}