package com.example.agenda.recyclers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.api.ActivitatResponseDto

class ActivitatAdapter(
    private var activitats: List<ActivitatResponseDto>,
    private val onItemClick: (ActivitatResponseDto) -> Unit,
    private val onItemLongClick: (ActivitatResponseDto, View) -> Unit
) : RecyclerView.Adapter<ActivitatHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ActivitatHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.activitat_layout, parent, false)
        return ActivitatHolder(view, onItemLongClick)
    }

    override fun onBindViewHolder(holder: ActivitatHolder, position: Int) {
        val activitat = activitats[position]
        holder.bind(activitat)
    }

    override fun getItemCount(): Int = activitats.size

    fun updateList(newList: List<ActivitatResponseDto>) {
        activitats = newList
        notifyDataSetChanged()
    }
}