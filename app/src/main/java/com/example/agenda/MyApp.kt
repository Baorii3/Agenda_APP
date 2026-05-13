package com.example.agenda

import android.app.Application
import com.example.agenda.api.Api

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.init(this)
    }
}