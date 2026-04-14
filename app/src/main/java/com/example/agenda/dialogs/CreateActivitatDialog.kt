package com.example.agenda.dialogs

import android.app.DatePickerDialog
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
        var listaProfes: List<UsuariResponseDto> = emptyList()
        lifecycleScope.launch {
            try {
                val response = Api.getUsuariService().getProfesores()
                if (response.isSuccessful) {
                    val listaDesdeApi = response.body() ?: emptyList()
                    listaProfes = listaDesdeApi
                    opcionesProvisionales.clear()
                    opcionesProvisionales.addAll(listaDesdeApi.map { it.email ?: "Sin email" })
                    adapter.notifyDataSetChanged()
                    Log.d("CreateActivitatDialog", "Profesores cargados: ${opcionesProvisionales.size}")
                } else {
                    Log.e("CreateActivitatDialog", "Error API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CreateActivitatDialog", "Fallo de conexión: ${e.message}")
            }
        }

        val etData = view.findViewById<EditText>(R.id.etData)
        val etHoraInici = view.findViewById<EditText>(R.id.etHoraInici)
        val etHoraFi = view.findViewById<EditText>(R.id.etHoraFi)

        etData.setOnClickListener {
            val c = java.util.Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),
                {_, year, month, day ->
                    val mesActual = month + 1
                    val fechaFormateada = String.format("%04d-%02d-%02d", year, mesActual, day)
                    etData.setText(fechaFormateada)
                },
                c.get(java.util.Calendar.YEAR),
                c.get(java.util.Calendar.MONTH),
                c.get(java.util.Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

         etHoraInici.setOnClickListener {
            val c = java.util.Calendar.getInstance()
            val timePicker = android.app.TimePickerDialog(requireContext(),
                {_, hourOfDay, minute ->
                    val horaFormateada = String.format("%02d:%02d", hourOfDay, minute)
                    etHoraInici.setText(horaFormateada)
                },
                c.get(java.util.Calendar.HOUR_OF_DAY),
                c.get(java.util.Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        etHoraFi.setOnClickListener {
            val c = java.util.Calendar.getInstance()
            val timePicker = android.app.TimePickerDialog(requireContext(),
                {_, hourOfDay, minute ->
                    val horaFormateada = String.format("%02d:%02d", hourOfDay, minute)
                    etHoraFi.setText(horaFormateada)
                },
                c.get(java.util.Calendar.HOUR_OF_DAY),
                c.get(java.util.Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        val etTitol = view.findViewById<EditText>(R.id.etTitol)
        val etDescripcio = view.findViewById<EditText>(R.id.etDescripcio)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val bundle = Bundle().apply {
                    putString("titol", etTitol.text.toString())
                    putString("descripcio", etDescripcio.text.toString())
                    putString("data", etData.text.toString())
                    putString("horaInici", etHoraInici.text.toString())
                    putString("horaFi", etHoraFi.text.toString())
                    val usuariSeleccionat = spinnerUsuari.selectedItem as String
                    val usuariId = listaProfes.find { it.email == usuariSeleccionat }?.idUsuari ?: -1
                    putLong("usuariId", usuariId)
                }
                parentFragmentManager.setFragmentResult("createActivitatRequest", bundle)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}