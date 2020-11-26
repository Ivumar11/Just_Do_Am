package com.example.justdoam.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.justdoam.Task
import java.util.*

@Dao
interface TaskDao {
    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()

    @Query("SELECT * FROM task")
    fun getAllTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id : UUID) : LiveData<Task?>
}