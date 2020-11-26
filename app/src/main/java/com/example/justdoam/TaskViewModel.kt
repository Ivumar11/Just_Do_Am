package com.example.justdoam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class TaskViewModel : ViewModel() {
    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()

    val taskLiveData : LiveData<Task?> = Transformations.switchMap(taskIdLiveData) {
        taskRepository.getTask(it)
    }

    var isNewTask = true

    var task = Task()

    var charSq = ""

    var justStarted = true

    fun loadTask(id: UUID) {
        taskIdLiveData.value = id
    }

    fun saveTask(task: Task) {
        taskRepository.updateTask(task)
    }

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }
}