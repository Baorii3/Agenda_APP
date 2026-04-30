package com.example.agenda.recyclers

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.api.SalaResponseDto

class SalaHolder(itemView: View, private val onItemClick: (SalaResponseDto) -> Unit, private val onItemLongClick: (SalaResponseDto, View) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<TextView>(com.example.agenda.R.id.tvSalaName)
    private val description = itemView.findViewById<TextView>(com.example.agenda.R.id.tvSalaDescription)

    fun bind(item: SalaResponseDto) {
        name.text = item.nom
        description.text = item.descripcio

        itemView.setOnClickListener {
            onItemClick(item)
        }
        itemView.setOnLongClickListener {
            onItemLongClick(item, itemView)
            true
        }
    }
}