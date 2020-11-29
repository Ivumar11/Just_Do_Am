package com.example.justdoam

import android.app.Application

/* Class created to make a one-time initialization of the database when the app is started.
Whatsoever is initialized here has the scope of the lifetime of the app.
AndroidManifest must be notified of this child Application, else the app will crash
*/

class JustDoAmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialized at the start of the app
        TaskRepository.initialize(this)
    }
}