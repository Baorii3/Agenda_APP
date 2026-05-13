package com.example.agenda.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
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
import java.util.*

class UpdateActivitatDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.update_activitat_dialog, null)

        val etTitol = view.findViewById<EditText>(R.id.etEditTitol)
        val etData = view.findViewById<EditText>(R.id.etEditData)
        val etHoraInici = view.findViewById<EditText>(R.id.etEditHoraInici)
        val etHoraFi = view.findViewById<EditText>(R.id.etEditHoraFi)
        val spinnerUsuari = view.findViewById<Spinner>(R.id.spinnerEditUsuari)
        val etDescripcio = view.findViewById<EditText>(R.id.etEditDescripcio)

        val id = arguments?.getLong("idActivitat") ?: -1L
        val titolActual = arguments?.getString("titol") ?: ""
        val descripcioActual = arguments?.getString("descripcio") ?: ""
        val dataActual = arguments?.getString("data") ?: ""
        val horaIniciActual = arguments?.getString("horaInici") ?: ""
        val horaFiActual = arguments?.getString("horaFi") ?: ""
        val usuariActualId = arguments?.getLong("usuariId") ?: -1L

        etTitol.setText(titolActual)
        etData.setText(dataActual)
        etHoraInici.setText(horaIniciActual)
        etHoraFi.setText(horaFiActual)
        etDescripcio.setText(descripcioActual)

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
                    val emails = listaDesdeApi.map { it.email }
                    opcionesProvisionales.clear()
                    opcionesProvisionales.addAll(emails)
                    adapter.notifyDataSetChanged()

                    val position = listaDesdeApi.indexOfFirst { it.idUsuari == usuariActualId }
                    if (position >= 0 && position < opcionesProvisionales.size) {
                        spinnerUsuari.setSelection(position)
                    }
                } else {
                    Log.e("UpdateActivitatDialog", "Error HTTP al cargar usuarios: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UpdateActivitatDialog", "Error cargando usuarios: ${e.message}")
                opcionesProvisionales.clear()
                adapter.notifyDataSetChanged()
            }
        }

        etData.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                val mesActual = month + 1
                val fechaFormateada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, mesActual, day)
                etData.setText(fechaFormateada)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        etHoraInici.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                etHoraInici.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        etHoraFi.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                etHoraFi.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton("Actualizar") { _, _ ->
                val usuariSeleccionado = spinnerUsuari.selectedItem as? String ?: ""
                val usuariId = listaProfes.find { it.email == usuariSeleccionado }?.idUsuari ?: -1L

                val bundle = Bundle().apply {
                    putLong("idActivitat", id)
                    putString("titol", etTitol.text.toString())
                    putString("descripcio", etDescripcio.text.toString())
                    putString("data", etData.text.toString())
                    putString("horaInici", etHoraInici.text.toString())
                    putString("horaFi", etHoraFi.text.toString())
                    putLong("usuariId", usuariId)
                }
                parentFragmentManager.setFragmentResult("updateActivitatRequest", bundle)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    companion object {
        fun newInstance(
            idActivitat: Long,
            titol: String,
            descripcio: String,
            data: String,
            horaInici: String,
            horaFi: String,
            usuariId: Long
        ): UpdateActivitatDialog {
            val frag = UpdateActivitatDialog()
            val args = Bundle()
            args.putLong("idActivitat", idActivitat)
            args.putString("titol", titol)
            args.putString("descripcio", descripcio)
            args.putString("data", data)
            args.putString("horaInici", horaInici)
            args.putString("horaFi", horaFi)
            args.putLong("usuariId", usuariId)
            frag.arguments = args
            return frag
        }
    }
}
