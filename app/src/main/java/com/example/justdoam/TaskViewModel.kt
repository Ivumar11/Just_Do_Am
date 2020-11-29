package com.example.justdoam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class  TaskViewModel : ViewModel() {
    // Gets the single instance of TaskRepository of the app
    private val taskRepository = TaskRepository.get()

    // Holds the id value of an already existing task.
    // It will be null if we are dealing with a new task.
    private val taskIdLiveData = MutableLiveData<UUID>()

    // Loads the appropriate task from the database when taskIdLiveData gets a value and stores it.
    // This variable will be observed by the associated fragment
    val taskLiveData : LiveData<Task?> = Transformations.switchMap(taskIdLiveData) {
        taskRepository.getTask(it)
    }

    // A new task or an already existing task?
    var isNewTask = true

    // This holds the task to be updated or added to the database
    var task = Task()

    // Used to keep track of the text in the editText view as user types
    var taskStringToBe = ""

    // Represents the state of the fragment, as in, did it just get launched?
    var justStarted = true

    // Used to get the id of an already existing task, assigning it to taskIdLiveData
    fun loadTask(id: UUID) {
        taskIdLiveData.value = id
    }

    // For updating a task
    fun saveTask(task: Task) {
        taskRepository.updateTask(task)
    }

    // For adding a new task
    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }
}