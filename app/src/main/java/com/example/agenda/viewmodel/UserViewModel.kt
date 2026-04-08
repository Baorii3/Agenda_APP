package com.example.agenda.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agenda.api.UsuariResponseDto

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



}