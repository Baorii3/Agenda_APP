package com.example.agenda.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.ActivitatResponseDto
import com.example.agenda.api.Api
import com.example.agenda.api.UsuariResponseDto
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UsuariResponseDto?>()
    val user: LiveData<UsuariResponseDto?> = _user

    val canCreateSala: LiveData<Boolean> = _user.map { currentUser -> canCreateSalas(currentUser) }

    private val _listasPropies = MutableLiveData<List<ActivitatResponseDto>>(emptyList())
    val listasPropies: LiveData<List<ActivitatResponseDto>> = _listasPropies

    fun canAccessDevices(): Boolean = canAccessDevices(_user.value)

    fun canAccessUsers(): Boolean = canAccessUsers(_user.value)

    fun canAccessReservations(): Boolean = canAccessReservations(_user.value)

    fun canCreateSalas(): Boolean = canCreateSalas(_user.value)

    fun canCreateActividades(): Boolean = canCreateActividades(_user.value)

    private fun canAccessDevices(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, DEVICE_PERMISSION_KEY, LEVEL_READ)
    }

    private fun canAccessUsers(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, USERS_PERMISSION_KEY, LEVEL_READ)
    }

    private fun canAccessReservations(currentUser: UsuariResponseDto?): Boolean {
        return isProfesor(currentUser)
    }

    private fun canCreateSalas(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, SALA_CREATE_PERMISSION_KEY, LEVEL_CREATE)
    }

    private fun canCreateActividades(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, ACTIVIDADES_CREATE_PERMISSION_KEY, LEVEL_CREATE)
    }

    private fun isAdmin(currentUser: UsuariResponseDto?): Boolean {
        return currentUser?.rol.equals("admin", ignoreCase = true)
    }

    private fun isProfesor(currentUser: UsuariResponseDto?): Boolean {
        return currentUser?.rol.equals("profesor", ignoreCase = true)
    }

    private fun hasPermission(currentUser: UsuariResponseDto?, key: String, minValue: Long = LEVEL_READ): Boolean {
        if (currentUser == null) return false
        return currentUser.permisos.any { permiso ->
            permiso.recurso.equals(key, ignoreCase = true) && permiso.valor >= minValue
        }
    }

    fun setUser(user: UsuariResponseDto?) {
        _user.postValue(user)
    }

    fun fetchUserData(
        authHeader: String,
        onAuthError: () -> Unit,
        onMessage: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = Api.getUsuariService().crearUsuario(authHeader)
                if (response.isSuccessful) {
                    setUser(response.body())
                    onMessage("Sesión cargada correctamente")
                } else {
                    onMessage("No se ha podido cargar el usuario")
                    onAuthError()
                }
            } catch (_: Exception) {
                onMessage("Error de red al cargar la sesión")
            }
        }
    }

    fun fetchListasPropies(onMessage: (String) -> Unit) {
        val currentUsuari = _user.value
        if (currentUsuari == null) {
            onMessage("No se puede cargar mis reservas sin usuario")
            return
        }

        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().llistaActivitatsByUsuari(currentUsuari.idUsuari)
                if (response.isSuccessful) {
                    _listasPropies.postValue(response.body() ?: emptyList())
                    onMessage("Mis reservas cargadas correctamente")
                } else {
                    onMessage("No se han podido cargar mis reservas")
                }
            } catch (_: Exception) {
                onMessage("Error de red al cargar mis reservas")
            }
        }
    }

    companion object {
        private const val LEVEL_READ = 1L
        private const val LEVEL_CREATE = 3L

        private const val DEVICE_PERMISSION_KEY = "dispositivos"
        private const val USERS_PERMISSION_KEY = "usuarios"
        private const val SALA_CREATE_PERMISSION_KEY = "salas_create"
        private const val ACTIVIDADES_CREATE_PERMISSION_KEY = "actividades_create"
    }
}