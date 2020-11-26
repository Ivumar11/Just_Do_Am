package com.example.justdoam

import androidx.lifecycle.ViewModel

class TodayViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getAllTasks()

    fun deleteAll() = taskRepository.deleteAllTasks()
    fun updateTask(task: Task) = taskRepository.updateTask(task)

}