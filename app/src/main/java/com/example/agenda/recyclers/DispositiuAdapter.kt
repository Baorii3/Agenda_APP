package com.example.agenda.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.DispositiuResponseDto

class DispositiuAdapter(
    private var dispositius: List<DispositiuResponseDto>,
    private val onItemClick: (DispositiuResponseDto) -> Unit
) : RecyclerView.Adapter<DispositiuHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DispositiuHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dispositiu_layout, parent, false)
        return DispositiuHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: DispositiuHolder, position: Int) {
        val dispositiu = dispositius[position]
        holder.bind(dispositiu)

    }

    override fun getItemCount(): Int = dispositius.size

    fun updateList(newList: List<DispositiuResponseDto>) {
        dispositius = newList
        notifyDataSetChanged()
    }
}