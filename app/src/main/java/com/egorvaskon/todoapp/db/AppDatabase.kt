package com.egorvaskon.todoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.egorvaskon.todoapp.Task

@Database(entities = [Task::class],version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
}