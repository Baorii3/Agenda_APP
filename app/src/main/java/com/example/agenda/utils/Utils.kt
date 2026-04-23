package com.example.agenda.utils

import android.view.View
import android.view.ViewGroup

fun View.applyPermissions(userCan: (String) -> Boolean) {

    if (this is ViewGroup) {
        for (i in 0 until childCount) {
            getChildAt(i).applyPermissions(userCan)
        }
    }

    val permission = tag as? String

    if (permission != null && !userCan(permission)) {
        visibility = View.GONE
    }
}