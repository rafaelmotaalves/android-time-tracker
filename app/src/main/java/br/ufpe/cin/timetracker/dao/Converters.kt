package br.ufpe.cin.timetracker.dao

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochSecond(value) }
    }

    @TypeConverter
    fun dateToTimestamp(instant: Instant?): Long? {
        return instant?.epochSecond
    }
}