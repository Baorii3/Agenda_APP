package com.example.agenda.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.viewmodel.SalaViewModel
import com.example.agenda.api.ActivitatRequestDto
import com.example.agenda.dialogs.CreateActivitatDialog
import com.example.agenda.recyclers.ActivitatAdapter

class SalaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var rvActivitat: RecyclerView
    private lateinit var adapter: ActivitatAdapter

    private val viewModel: SalaViewModel by activityViewModels<SalaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sala, container, false)

        val salaId = arguments?.getLong("salaId") ?: -1L
        if (salaId == -1L) {
            Log.d("SalaFragment", "No se recibió un ID de sala válido")
            return view
        }
        val sala = viewModel.findSalaById(salaId)
        val tvSalaName = view.findViewById<TextView>(R.id.tvSalaTitle)
        val tvSalaDescription = view.findViewById<TextView>(R.id.tvSalaDescription)
        tvSalaName.text = sala?.nom
        tvSalaDescription.text = sala?.descripcio
        viewModel.cargarActivitatsForSala(salaId)
        adapter = ActivitatAdapter(
            activitats = emptyList(),
            onItemClick = {}
        )
        rvActivitat = view.findViewById(R.id.activitatsRecyclerView)
        rvActivitat.layoutManager = LinearLayoutManager(context)
        rvActivitat.adapter = adapter
        viewModel.activitats.observe(viewLifecycleOwner) { activitats ->
            adapter.updateList(activitats)
        }

        val btnAdd = view.findViewById<ImageView>(R.id.addactivitatButton)
        btnAdd.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag("CreateActivitatDialog") == null) {
                CreateActivitatDialog().show(parentFragmentManager, "CreateActivitatDialog")
            }
        }

        parentFragmentManager.setFragmentResultListener("createActivitatRequest", this) { _, bundle ->
            val titol = bundle.getString("titol")
            val descripcio = bundle.getString("descripcio")
            val data = bundle.getString("data")
            val horaInici = bundle.getString("horaInici")
            val horaFi = bundle.getString("horaFi")
            val professorId = bundle.getLong("usuariId")
            val actRequest = ActivitatRequestDto(
                idSala = salaId,
                idUsuari = professorId,
                titol = titol ?: "",
                descripcio = descripcio ?: "",
                data = data ?: "",
                horaInici = horaInici ?: "",
                horaFi = horaFi ?: ""
            )
            viewModel.crearActivitat(actRequest)
        }

        return view


    }
}