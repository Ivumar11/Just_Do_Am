package com.example.justdoam

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
/* This is the model class that contains the structure of the database table.
Used by TaskDatabase */
data class Task (@PrimaryKey val id : UUID = UUID.randomUUID(), var taskString: String = "", var isDone : Boolean = false)