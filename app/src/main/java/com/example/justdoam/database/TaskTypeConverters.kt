package com.example.justdoam.database

import androidx.room.TypeConverter
import java.util.*

class TaskTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?) : String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?) : UUID? {
        return UUID.fromString(uuid)
    }
}