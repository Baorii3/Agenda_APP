package com.example.agenda.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aws.smithy.kotlin.runtime.telemetry.context.Context
import com.example.agenda.api.Api
import com.example.agenda.api.DispositiuResponseDto
import kotlinx.coroutines.launch
import okhttp3.internal.platform.PlatformRegistry.applicationContext

class DispositiuViewModel : ViewModel() {

    private val _dispositius = MutableLiveData<MutableList<DispositiuResponseDto>>()
    val dispositius: MutableLiveData<MutableList<DispositiuResponseDto>> = _dispositius

    fun cargarDispositiusApi() {
        viewModelScope.launch {
            try {
                val response = Api.getDispositiuService().llistaDispositius()
                if (response.isSuccessful) {
                    val items = response.body()
                    _dispositius.postValue(items?.toMutableList() ?: mutableListOf())
                } else {
                    Log.e("API_DISP", "Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_DISP", "Error de conexió", e)
            }
        }
    }
}