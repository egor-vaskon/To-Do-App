package com.egorvaskon.todoapp.db

import androidx.room.*
import com.egorvaskon.todoapp.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY text ASC")
    fun getTasksSortedByText(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY date ASC")
    fun getAllTasksSortedByDate(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}