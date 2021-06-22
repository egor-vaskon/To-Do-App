package com.egorvaskon.todoapp.db

import com.egorvaskon.todoapp.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val tasksSortedByDate: Flow<List<Task>> = taskDao.getAllTasksSortedByDate()
    val tasksSortedByText: Flow<List<Task>> = taskDao.getTasksSortedByText()

    suspend fun addTask(task: Task){
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

}