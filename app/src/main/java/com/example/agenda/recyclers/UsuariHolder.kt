package com.example.agenda.recyclers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.UsuariResponseDto

class UsuariHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<TextView>(R.id.tvUsuariName)
    private val email = itemView.findViewById<TextView>(R.id.tvUsuariEmail)
    private val rol = itemView.findViewById<TextView>(R.id.tvUsuariRol)

    fun bind(item: UsuariResponseDto) {
        name.text = item.nom
        email.text = item.email
        rol.text = item.rol

    }
}
