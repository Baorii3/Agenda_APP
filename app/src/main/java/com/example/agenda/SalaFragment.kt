package com.example.agenda

import SalaViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.api.Api
import com.example.agenda.api.SalaResponseDto
import com.example.agenda.recyclers.ActivitatAdapter
class SalaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var rvActivitat: RecyclerView
    private lateinit var adapter: ActivitatAdapter

    private val viewModel: SalaViewModel by activityViewModels()

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
        return view


    }
}