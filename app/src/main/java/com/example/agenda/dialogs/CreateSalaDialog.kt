package com.example.agenda.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.agenda.R
import com.example.agenda.api.PisoSala
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial

class CreateSalaDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.create_sala_dialog, null)

        val etNom = view.findViewById<EditText>(R.id.etNom)
        val spinnerUbicacio = view.findViewById<Spinner>(R.id.spinnerUbicacio)
        val etDescripcio = view.findViewById<EditText>(R.id.etDescripcio)

        val opciones = PisoSala.values().map { it.name }.toTypedArray()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, opciones)
        spinnerUbicacio.adapter = adapter

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