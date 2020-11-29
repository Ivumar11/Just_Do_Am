package com.example.justdoam.database

import androidx.room.TypeConverter
import java.util.*

/* Used ti specify how the database should store and give out data of non-primitives.
This is used by TaskDatabase.
*/
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