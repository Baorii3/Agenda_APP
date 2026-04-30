package com.example.agenda.utils

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.example.agenda.R

object MenuLongClickUtils {

    fun showMenuLongClick(context: Context, view: View, menuId: Int, onEdit: () -> Unit, onDelete: () -> Unit) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.menu_long_click)

        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.pop_editar -> {
                    onEdit()
                    true
                }
                R.id.pop_eliminar -> {
                    onDelete()
                    false
                }
                else -> false
            }
        }
    }
}