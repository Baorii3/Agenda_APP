package com.example.agenda.recyclers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.SalaResponseDto

class SalaAdapter(
    private var salas: List<SalaResponseDto>,
    private val onItemClick: (SalaResponseDto) -> Unit,
    private val onItemLongClick: (SalaResponseDto, View) -> Unit
) : RecyclerView.Adapter<SalaHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): SalaHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.sala_layout, parent, false)
        return SalaHolder(view, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: SalaHolder, position: Int) {
        val sala = salas[position]
        holder.bind(sala)
    }

    override fun getItemCount(): Int = salas.size

    fun updateList(newList: List<SalaResponseDto>) {
        salas = newList
        notifyDataSetChanged()
    }
}