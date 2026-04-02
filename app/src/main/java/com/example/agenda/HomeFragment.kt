package com.example.agenda

import SalaViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.recyclers.SalaAdapter
class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var rvSalas: RecyclerView
    private lateinit var adapter: SalaAdapter

    private val viewModel: SalaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel.cargarSalasApi()
        adapter = SalaAdapter(
            salas = emptyList() ,
            onItemClick = { item ->
                val bundle = Bundle()
                bundle.putLong("salaId", item.id)

                val fragment = SalaFragment()
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()

            }
        )
        val rvSalas = view.findViewById<RecyclerView>(R.id.salasRecyclerView)
        rvSalas.layoutManager = LinearLayoutManager(context)
        rvSalas.adapter = adapter

        viewModel.salas.observe(viewLifecycleOwner) { salas ->
            adapter.updateList(salas)
        }
        return view
    }
}