package com.example.agenda.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.UsuariResponseDto

class UsuariAdapter(
    private var usuaris: List<UsuariResponseDto>
) : RecyclerView.Adapter<UsuariHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuari_item, parent, false)
        return UsuariHolder(view)
    }

    override fun onBindViewHolder(holder: UsuariHolder, position: Int) {
        holder.bind(usuaris[position])
    }

    override fun getItemCount(): Int = usuaris.size

    fun updateList(nova: List<UsuariResponseDto>) {
        usuaris = nova
        notifyDataSetChanged()
    }
}

