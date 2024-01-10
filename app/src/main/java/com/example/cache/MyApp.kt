package com.example.cache

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MyApp : Application() {

    companion object {
        lateinit var database: AppDatabase
        lateinit var userRepository: UserRepository
    }

    override fun onCreate() {
        super.onCreate()

        // Inicialize o Firebase
        FirebaseApp.initializeApp(this)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        userRepository = UserRepository(database.userDao(), FirebaseFirestore.getInstance())
    }
}
