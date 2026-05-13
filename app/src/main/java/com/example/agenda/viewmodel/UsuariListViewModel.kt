package com.example.agenda.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.api.Api
import com.example.agenda.api.UsuariResponseDto
import kotlinx.coroutines.launch

class UsuariListViewModel : ViewModel() {
    private val _usuaris = MutableLiveData<List<UsuariResponseDto>>(emptyList())
    val usuaris: LiveData<List<UsuariResponseDto>> = _usuaris

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _grouped = MutableLiveData<Map<String, List<UsuariResponseDto>>>(emptyMap())
    val grouped: LiveData<Map<String, List<UsuariResponseDto>>> = _grouped

    fun cargarUsuaris() {
        viewModelScope.launch {
            _error.postValue(null)
            try {
                val response = Api.getUsuariService().getUsuaris()
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    _usuaris.postValue(list)
                    _grouped.postValue(groupByRole(list))
                } else {
                    Log.e("UsuariListViewModel", "Error HTTP: ${response.code()}")
                    _error.postValue("No se pudieron cargar los usuarios")
                }
            } catch (e: Exception) {
                Log.e("UsuariListViewModel", "Error de conexión", e)
                _error.postValue("Error de conexión al cargar usuarios")
            }
        }
    }

    private fun groupByRole(list: List<UsuariResponseDto>): Map<String, List<UsuariResponseDto>> {
        return list.groupBy { it.rol }
    }
}
