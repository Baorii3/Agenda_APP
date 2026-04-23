package com.example.agenda.recyclers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.api.ActivitatResponseDto

class ActivitatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<TextView>(com.example.agenda.R.id.activitatName)
    private val description = itemView.findViewById<TextView>(com.example.agenda.R.id.activitatDescription)

    private val time = itemView.findViewById<TextView>(com.example.agenda.R.id.tvTime)

    fun bind(item: ActivitatResponseDto) {
        name.text = item.titol
        description.text = item.descripcio
        time.text = "${item.horaInici.substring(0,6)} - ${item.horaFi.substring(0,5)}"
        //time.text = item.time

        //itemView.setOnClickListener {
         //   onItemClick(item)
       // }
    }
}