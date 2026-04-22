package com.example.agenda.recyclers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.DispositiuResponseDto

class DispositiuHolder(
    itemView: View,
    private val onItemClick: (DispositiuResponseDto) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val nombre = itemView.findViewById<TextView>(R.id.tvDispositivoNombre)
    private val categoria = itemView.findViewById<TextView>(R.id.tvDispositivoCategoria)
    private val serial = itemView.findViewById<TextView>(R.id.tvDispositivoSerial)
    private val estado = itemView.findViewById<TextView>(R.id.tvDispositivoEstado)

    fun bind(item: DispositiuResponseDto) {

        nombre.text = item.nom
        categoria.text = item.tipus
        serial.text = "ID: ${item.id}"

        if (item.actiu.toString().uppercase() == false.toString().uppercase()) {
            estado.text = "OFFLINE"
        } else{
            estado.text = "ONLINE"
        }
        //itemView.setOnClickListener {
        //   onItemClick(item)
        // }
    }
}