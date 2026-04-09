package com.example.agenda.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.Api
import com.example.agenda.api.UsuariResponseDto
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UsuariResponseDto?>()
    val user: LiveData<UsuariResponseDto?> = _user

    private val _isLogged = MutableLiveData<Boolean>(false)
    val isLogged: LiveData<Boolean> = _isLogged

    init {
        _user.postValue(null)
    }
    fun setEstaLogueado(isLogged: Boolean) {
        _isLogged.value = isLogged

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


}