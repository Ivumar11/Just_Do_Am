package com.example.justdoam.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.justdoam.Task
import java.util.*

// This interface contains methods we use to interact with the database.
@Dao
interface TaskDao {
    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()

    /* When fetching data from the database, we wrap the return type within LiveData. This ensures that the operation is carried out
    in the background thread.
    It also ensures that any variable assigned to the return value of the function will automatically get updated on any change made to
    the database.
     */

    @Query("SELECT * FROM task")
    fun getAllTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id : UUID) : LiveData<Task?>
}