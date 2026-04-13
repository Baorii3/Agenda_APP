package com.example.agenda.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.agenda.R
import com.example.agenda.api.Api
import com.example.agenda.api.UsuariResponseDto
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class CreateActivitatDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.create_activitat_dialog, null)

        val spinnerUsuari = view.findViewById<Spinner>(R.id.spinnerUsuari)
        val opcionesProvisionales = mutableListOf("Cargando...")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, opcionesProvisionales)
        spinnerUsuari.adapter = adapter

        var listaProfes: List<UsuariResponseDto>
        lifecycleScope.launch {
            try {
                val response = Api.getUsuariService().getProfesores()
                if (response.isSuccessful) {
                    val listaDesdeApi = response.body() ?: emptyList()
                    listaProfes = listaDesdeApi
                    opcionesProvisionales.clear()
                    opcionesProvisionales.addAll(listaDesdeApi.map { it.nom ?: "Sin nombre" })
                    adapter.notifyDataSetChanged()
                    Log.d("CreateActivitatDialog", "Profesores cargados: ${opcionesProvisionales.size}")
                } else {
                    Log.e("CreateActivitatDialog", "Error API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CreateActivitatDialog", "Fallo de conexión: ${e.message}")
            }
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val bundle = Bundle().apply {
                }
                parentFragmentManager.setFragmentResult("createActivitatRequest", bundle)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}