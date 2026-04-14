package com.example.agenda.recyclers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.api.ActivitatResponseDto

class ReservaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titol = itemView.findViewById<TextView>(com.example.agenda.R.id.tvReservaTitol)
    val sala = itemView.findViewById<TextView>(com.example.agenda.R.id.tvReservaSala)
    val data = itemView.findViewById<TextView>(com.example.agenda.R.id.tvReservaData)
    val hora = itemView.findViewById<TextView>(com.example.agenda.R.id.tvReservaHora)
    fun bind(item: ActivitatResponseDto) {
        titol.text = item.titol
        sala.text = item.nomSala
        data.text = item.data
        hora.text = item.horaInici + " - " + item.horaFi
    }
}