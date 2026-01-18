package com.example.sayurin

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SayurinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Paksa inisialisasi Firebase saat aplikasi pertama kali berjalan
        FirebaseApp.initializeApp(this)
    }
}
