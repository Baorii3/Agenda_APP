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

class UpdateSalaDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.update_sala_dialog, null)

        val etNom = view.findViewById<EditText>(R.id.etEditNom)
        val spinnerUbicacio = view.findViewById<Spinner>(R.id.spinnerEditUbicacio)
        val etDescripcio = view.findViewById<EditText>(R.id.etEditDescripcio)

        val id = arguments?.getLong("id") ?: -1L
        val nomActual = arguments?.getString("nom") ?: ""
        val ubicacioActual = arguments?.getString("ubicacio") ?: ""
        val descripcioActual = arguments?.getString("descripcio") ?: ""

        etNom.setText(nomActual)
        etDescripcio.setText(descripcioActual)

        val opcionesProvisionales = mutableListOf("Cargando...")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, opcionesProvisionales)
        spinnerUbicacio.adapter = adapter

        lifecycleScope.launch {
            try {
                val pisosDesdeApi = Api.getDiccionariService().getPisos()
                opcionesProvisionales.clear()
                opcionesProvisionales.addAll(pisosDesdeApi)
                adapter.notifyDataSetChanged()

                val position = pisosDesdeApi.indexOf(ubicacioActual)
                if (position != -1) {
                    spinnerUbicacio.setSelection(position)
                }

            } catch (e: Exception) {
                Log.e("UpdateSalaDialog", "Error cargando pisos: ${e.message}")
                opcionesProvisionales.clear()
                adapter.notifyDataSetChanged()
            }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton("Actualizar") { _, _ ->
                val bundle = Bundle().apply {
                    putLong("id", id)
                    putString("nom", etNom.text.toString())
                    putString("ubicacio", spinnerUbicacio.selectedItem.toString())
                    putString("descripcio", etDescripcio.text.toString())
                }
                parentFragmentManager.setFragmentResult("updateSalaRequest", bundle)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    companion object {
        fun newInstance(id:Long, nom: String, ubicacio: String, descripcio: String): UpdateSalaDialog {
            val frag = UpdateSalaDialog()
            val args = Bundle()
            args.putLong("id", id)
            args.putString("nom", nom)
            args.putString("ubicacio", ubicacio)
            args.putString("descripcio", descripcio)
            frag.arguments = args
            return frag
        }
    }
}