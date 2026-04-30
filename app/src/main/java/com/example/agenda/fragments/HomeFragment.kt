package com.example.agenda.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.viewmodel.SalaViewModel
import com.example.agenda.api.SalaRequestDto
import com.example.agenda.dialogs.CreateSalaDialog
import com.example.agenda.recyclers.SalaAdapter
import com.example.agenda.utils.applyPermissions
import com.example.agenda.viewmodel.Permission
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
            },
            onItemLongClick = { item, view ->
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.menu_long_click, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.pop_editar -> {
                            //
                            true
                        }
                        R.id.pop_eliminar -> {
                            viewModel.deleteSalas(item.id)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        )
        rvSalas = view.findViewById<RecyclerView>(R.id.salasRecyclerView)
        rvSalas.layoutManager = LinearLayoutManager(context)
        rvSalas.adapter = adapter

        viewModel.salas.observe(viewLifecycleOwner) { salas ->
            adapter.updateList(salas)
        }

        viewModelUsuari.permissions.observe(viewLifecycleOwner) {
            view.applyPermissions { permission ->
                Log.d("HomeFragment", "Checking permission: $permission")
                viewModelUsuari.userCan(Permission.valueOf(permission))
            }
        }

        val btnAdd = view.findViewById<ImageView>(R.id.addSalaButton)
        btnAdd.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag("CreateSalaDialog") == null) {
                CreateSalaDialog().show(parentFragmentManager, "CreateSalaDialog")
            }
        }

        parentFragmentManager.setFragmentResultListener("createSalaRequest", this) { _, bundle ->
            val nom = bundle.getString("nom")
            val ubicacio = bundle.getString("ubicacio")
            val descripcio = bundle.getString("descripcio")
            val sala = SalaRequestDto(
                nom = nom ?: "",
                ubicacio = ubicacio ?: "P0",
                descripcio = descripcio ?: "",
            )
            viewModel.addSala(sala)
        }
        return view
    }


}