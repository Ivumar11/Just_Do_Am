package com.example.justdoam
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.justdoam.database.TaskDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "task-database"
class TaskRepository private constructor(context: Context) {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun addTask(task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }
    fun deleteAllTasks() {
        executor.execute {
            taskDao.deleteAll()
        }
    }
    fun updateTask(task: Task) {
        executor.execute{
            taskDao.updateTask(task)
        }
    }
    fun getTask(id: UUID) : LiveData<Task?> = taskDao.getTask(id)
    fun getAllTasks() : LiveData<List<Task>> = taskDao.getAllTasks()

    companion object {
        private var INSTANCE: TaskRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get() : TaskRepository {
            return INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}