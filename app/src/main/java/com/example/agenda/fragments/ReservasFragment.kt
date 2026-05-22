package com.example.agenda.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
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
        val view = inflater.inflate(R.layout.fragment_reservas, container, false)
        rvReservas = view.findViewById(R.id.rvMisReservas)
        adapter = ReservaAdapter(
            reservas = emptyList(),
            onItemClick = { _ ->
            }
        )
        rvReservas.layoutManager = LinearLayoutManager(context)
        rvReservas.adapter = adapter

        viewModelUser.fetchListasPropies { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        viewModelUser.listasPropies.observe(viewLifecycleOwner) { reservas ->
            adapter.updateList(reservas)
        }
        return view
    }
}