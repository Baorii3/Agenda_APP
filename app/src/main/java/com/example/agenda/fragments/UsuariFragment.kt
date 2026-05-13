package com.example.agenda.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.recyclers.UsuariAdapter
import com.example.agenda.viewmodel.UsuariListViewModel

class UsuariFragment : Fragment() {

    private lateinit var rvUsuaris: RecyclerView
    private lateinit var pbUsuaris: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: UsuariAdapter
    private val viewModel: UsuariListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_usuari, container, false)

        rvUsuaris = view.findViewById(R.id.usuariRecyclerView)
        pbUsuaris = view.findViewById(R.id.pbUsuaris)
        tvEmpty = view.findViewById(R.id.tvUsuarisEmpty)

        rvUsuaris.layoutManager = LinearLayoutManager(context)
        adapter = UsuariAdapter(emptyList())
        rvUsuaris.adapter = adapter

        viewModel.usuaris.observe(viewLifecycleOwner) { usuaris ->
            adapter.updateList(usuaris)
            val empty = usuaris.isEmpty()
            tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
            if (empty) tvEmpty.text = getString(R.string.usuarios_empty)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrBlank()) {
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = errorMsg
            }
        }

        viewModel.cargarUsuaris()
        return view
    }
}
