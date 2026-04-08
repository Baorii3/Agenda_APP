package com.example.agenda

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.core.Amplify
import com.example.agenda.recyclers.SalaAdapter
import com.example.agenda.viewmodel.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var rvSalas: RecyclerView
    private lateinit var adapter: SalaAdapter

    private val viewModel: SalaViewModel by activityViewModels<SalaViewModel>()
    private val viewModelUsuari: UserViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel.cargarSalasApi()
        adapter = SalaAdapter(
            salas = emptyList(),
            onItemClick = { item ->
                val bundle = Bundle()
                bundle.putLong("salaId", item.id)

                val fragment = SalaFragment()
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        )
        rvSalas = view.findViewById<RecyclerView>(R.id.salasRecyclerView)
        rvSalas.layoutManager = LinearLayoutManager(context)
        rvSalas.adapter = adapter

        viewModel.salas.observe(viewLifecycleOwner) { salas ->
            adapter.updateList(salas)
        }

        val btnAdd = view.findViewById<ImageView>(R.id.addSalaButton)

        viewModelUsuari.isLogged.observe(viewLifecycleOwner) { logged ->
            if (logged) {
                btnAdd.visibility = View.VISIBLE
            } else {
                btnAdd.visibility = View.GONE
            }
        }

        return view
    }


}