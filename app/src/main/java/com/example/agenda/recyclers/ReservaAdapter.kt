package com.example.agenda.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.ActivitatResponseDto

class ReservaAdapter(
    private var reservas: List<ActivitatResponseDto>,
    private val onItemClick: (ActivitatResponseDto) -> Unit
) : RecyclerView.Adapter<ReservaHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ReservaHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.reserva_layout, parent, false)
        return ReservaHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaHolder, position: Int) {
        val reserva = reservas[position]
        holder.bind(reserva)
    }

    override fun getItemCount(): Int = reservas.size

    fun updateList(newList: List<ActivitatResponseDto>) {
        reservas = newList
        notifyDataSetChanged()
    }
}