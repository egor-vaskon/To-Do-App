package com.egorvaskon.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(@PrimaryKey(autoGenerate = false) val date: Long,
                val text: String,
                var done: Boolean = false)