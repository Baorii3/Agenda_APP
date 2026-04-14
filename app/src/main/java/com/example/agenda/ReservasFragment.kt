package com.example.agenda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.recyclers.ReservaAdapter
import com.example.agenda.viewmodel.UserViewModel

class ReservasFragment : Fragment() {

    private val viewModelUser: UserViewModel by activityViewModels<UserViewModel>()

    private lateinit var rvReservas: RecyclerView
    private lateinit var adapter: ReservaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reservas, container, false)
        rvReservas = view.findViewById(R.id.rvMisReservas)
        adapter = ReservaAdapter(
            reservas = emptyList(),
            onItemClick = { item ->
            }
        )
        rvReservas.layoutManager = LinearLayoutManager(context)
        rvReservas.adapter = adapter

        viewModelUser.fetchListasPropias()
        viewModelUser.listasPropias.observe(viewLifecycleOwner) { reservas ->
            adapter.updateList(reservas)
        }
        return view
    }
}