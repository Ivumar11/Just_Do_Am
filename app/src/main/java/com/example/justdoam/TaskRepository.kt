package com.example.justdoam
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.justdoam.database.TaskDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

// pass this value to Room.databaseBuilder. To ensure accuracy, we declare a constant for the string
private const val DATABASE_NAME = "task-database"
// This class represents our repository for the entire app, Our single source of truth.
// Private constructor because we don't want it called elsewhere
class TaskRepository private constructor(context: Context) {

    // TaskDatabase is built and assigned to [database]
    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()
    // reps TaskDao
    private val taskDao = database.taskDao()
    // Used to execute tasks in the background
    private val executor = Executors.newSingleThreadExecutor()

    // All methods for accessing and querying the database
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

    // Singleton... so that the class can have only one instance throughout the app
    companion object {
        private var INSTANCE: TaskRepository? = null
        // Called once in JustDoAmApplication to initialize the TaskRepository
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        // Used to get the instance of TaskRepository from anywhere in the app
        fun get() : TaskRepository {
            return INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}