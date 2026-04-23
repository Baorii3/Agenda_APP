package com.example.agenda.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.recyclers.DispositiuAdapter
import com.example.agenda.recyclers.ReservaAdapter
import com.example.agenda.viewmodel.DispositiuViewModel
import com.example.agenda.viewmodel.UserViewModel

class DispositiuFragment : Fragment() {

    private val viewModelUser: DispositiuViewModel by activityViewModels<DispositiuViewModel>()

    private lateinit var rvReservas: RecyclerView
    private lateinit var adapter: DispositiuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dispositiu, container, false)
        rvReservas = view.findViewById(R.id.recyclerDispositivos)
        adapter = DispositiuAdapter(
            dispositius = emptyList(),
            onItemClick = { item ->
            }
        )
        rvReservas.layoutManager = LinearLayoutManager(context)
        rvReservas.adapter = adapter

        viewModelUser.cargarDispositiusApi()
        viewModelUser.dispositius.observe(viewLifecycleOwner) { dispositius ->
            adapter.updateList(dispositius)
        }
        return view
    }
}