package com.example.justdoam

import androidx.lifecycle.ViewModel

class TodayViewModel : ViewModel() {
    // Gets the single instance of TaskRepository of the app
    private val taskRepository = TaskRepository.get()

    // LiveData holding all the tasks in the database.
    val taskListLiveData = taskRepository.getAllTasks()

    // As name suggests
    fun deleteAll() = taskRepository.deleteAllTasks()
    fun updateTask(task: Task) = taskRepository.updateTask(task)

}