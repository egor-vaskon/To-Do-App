package com.egorvaskon.todoapp

import android.app.Application
import androidx.room.Room
import com.egorvaskon.todoapp.db.AppDatabase
import com.egorvaskon.todoapp.db.TaskRepository

class App : Application(){

    companion object{
        private const val APP_DATABASE = "app_database"

        private lateinit var INSTANCE: App

        private val localDatabase by lazy{
            Room.databaseBuilder(INSTANCE,AppDatabase::class.java, APP_DATABASE).build()
        }

        val taskRepository by lazy{
            TaskRepository(localDatabase.taskDao())
        }
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

}