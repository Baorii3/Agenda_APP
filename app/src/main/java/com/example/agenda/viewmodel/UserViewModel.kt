package com.example.agenda.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.ActivitatResponseDto
import com.example.agenda.api.Api
import com.example.agenda.api.PermisoDto
import com.example.agenda.api.UsuariResponseDto
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UsuariResponseDto?>()
    val user: LiveData<UsuariResponseDto?> = _user

    private val _listasPropias = MutableLiveData<List<ActivitatResponseDto>>(emptyList())
    val listasPropias: LiveData<List<ActivitatResponseDto>> = _listasPropias

    private val _permissions = MutableLiveData<List<PermisoDto>>()
    val permissions: LiveData<List<PermisoDto>> = _permissions

    fun userCan(permission: PermisoDto): Boolean {
        Log.d("UserViewModel", "Checking permission: $permission")
        return _permissions.value?.contains(permission) ?: false
    }

    fun setUser(user: UsuariResponseDto?) {
        _user.postValue(user)
        Log.d("UserViewModel", "User set: $user")
    }

    fun fetchUserData(authHeader : String, onAuthError:() -> Unit) {
        viewModelScope.launch {
            try {
                val response = Api.getUsuariService().crearUsuario(authHeader)
                if (response.isSuccessful) {
                    Log.i("API", "Usuario cargado: ${response.body()}")
                    setUser(response.body())
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                    onAuthError()
                }
            } catch (e: Exception) {
                Log.e("API", "Error de red", e)
            }
        }
    }

    fun fetchListasPropias() {
        if (user.value == null) {
            Log.w("UserViewModel", "No user set, cannot fetch listas propias")
            return
        }
        viewModelScope.launch {
            try {
                val response = Api.getActivitatService().llistaActivitatsByUsuari(user.value!!.idUsuari)
                if (response.isSuccessful) {
                    _listasPropias.postValue(response.body() ?: emptyList())
                    Log.d("UserViewModel", "Listas propias cargadas: ${response.body()?.size ?: 0}")
                } else {
                    Log.e("API", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error de red", e)
            }
        }
    }
}