package com.example.justdoam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.justdoam.Task

/* The abstract class, TaskDatabase will be automatically implemented using the information provided by the anotations below, when
RoomDatabasebuilder is used on it. We do that in the TaskRepository class
 */

@Database(entities = [Task::class], version = 1)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase : RoomDatabase() {
    // The implementation will be generated when this database is built
    abstract fun taskDao() : TaskDao
}