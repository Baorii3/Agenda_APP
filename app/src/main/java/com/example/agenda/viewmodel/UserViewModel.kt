package com.example.agenda.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.ActivitatResponseDto
import com.example.agenda.api.Api
import com.example.agenda.api.PermisoDto
import com.example.agenda.api.UsuariResponseDto
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UsuariResponseDto?>()
    val user: LiveData<UsuariResponseDto?> = _user

    val canCreateSala: LiveData<Boolean> = _user.map { currentUser -> canCreateSalas(currentUser) }

    private val _listasPropies = MutableLiveData<List<ActivitatResponseDto>>(emptyList())
    val listasPropies: LiveData<List<ActivitatResponseDto>> = _listasPropies

    fun userCan(permission: PermisoDto): Boolean {
        Log.d("UserViewModel", "Checking permission: $permission")
        return hasPermission(_user.value, permission.recurso, permission.valor)
    }

    fun canAccessDevices(): Boolean = canAccessDevices(_user.value)

    fun canAccessUsers(): Boolean = canAccessUsers(_user.value)

    fun canCreateSalas(): Boolean = canCreateSalas(_user.value)

    private fun canAccessDevices(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, DEVICE_PERMISSION_KEY, LEVEL_READ)
    }

    private fun canAccessUsers(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, USERS_PERMISSION_KEY, LEVEL_READ)
    }

    private fun canCreateSalas(currentUser: UsuariResponseDto?): Boolean {
        return isAdmin(currentUser) || hasPermission(currentUser, SALA_CREATE_PERMISSION_KEY, LEVEL_CREATE)
    }

    private fun isAdmin(currentUser: UsuariResponseDto?): Boolean {
        return currentUser?.rol.equals("admin", ignoreCase = true)
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

    fun fetchUserData(authHeader : String, onAuthError:() -> Unit) {
        viewModelScope.launch {
            try {
                val response = Api.getUsuariService().crearUsuario(authHeader)
                if (response.isSuccessful) {
                    Log.i("APIFETCH", "Usuario cargado: ${response.body()}")
                    setUser(response.body())
                } else {
                    Log.e("APIFETCH", "Error HTTP: ${response.code()}")
                    onAuthError()
                }
            } catch (e: Exception) {
                Log.e("APIFETCH", "Error de red", e)
            }
        }
    }

    fun fetchListasPropies() {
        val currentUsuari = _user.value
        if (currentUsuari == null) {
            Log.w("UserViewModel", "No se puede cargar las listas propias sin un usuario válido")
            return
        }

        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().llistaActivitatsByUsuari(currentUsuari.idUsuari)
                if (response.isSuccessful) {
                    _listasPropies.postValue(response.body() ?: emptyList())
                    Log.d("UserViewModel", "Listas propias cargadas: ${response.body()?.size ?: 0}")
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de red", e)
            }
        }
    }

    companion object {
        private const val LEVEL_READ = 1L
        private const val LEVEL_CREATE = 3L

        private const val DEVICE_PERMISSION_KEY = "dispositivos"
        private const val USERS_PERMISSION_KEY = "usuarios"
        private const val SALA_CREATE_PERMISSION_KEY = "salas_create"
    }
}