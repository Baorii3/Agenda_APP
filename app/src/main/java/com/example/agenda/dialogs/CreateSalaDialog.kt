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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class CreateSalaDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.create_sala_dialog, null)

        val etNom = view.findViewById<EditText>(R.id.etNom)
        val spinnerUbicacio = view.findViewById<Spinner>(R.id.spinnerUbicacio)
        val etDescripcio = view.findViewById<EditText>(R.id.etDescripcio)
        val opcionesProvisionales = mutableListOf("Cargando...")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, opcionesProvisionales)
        spinnerUbicacio.adapter = adapter
        lifecycleScope.launch {
            try {
                val pisosDesdeApi = Api.getDiccionariService().getPisos()
                opcionesProvisionales.clear()
                opcionesProvisionales.addAll(pisosDesdeApi)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("CreateSalaDialog", "Error cargando pisos: ${e.message}")
                opcionesProvisionales.clear()
                adapter.notifyDataSetChanged()
            }
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton("Crear") { _, _ ->
                val bundle = Bundle().apply {
                    putString("nom", etNom.text.toString())
                    putString("ubicacio", spinnerUbicacio.selectedItem.toString())
                    putString("descripcio", etDescripcio.text.toString())
                }
                Log.d("CreateSalaDialog", "Datos ingresados: ${bundle.getString("nom")}, ${bundle.getString("ubicacio")}, ${bundle.getString("descripcio")}")
                parentFragmentManager.setFragmentResult("createSalaRequest", bundle)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}