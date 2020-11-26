package com.example.justdoam

import android.app.Application

class JustDoAmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}