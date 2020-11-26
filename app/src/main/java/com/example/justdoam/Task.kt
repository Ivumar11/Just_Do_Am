package com.example.justdoam

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task (@PrimaryKey val id : UUID = UUID.randomUUID(), var taskString: String = "", var isDone : Boolean = false)