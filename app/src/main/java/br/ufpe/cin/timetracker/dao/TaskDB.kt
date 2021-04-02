package br.ufpe.cin.timetracker.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.ufpe.cin.timetracker.model.TaskModel
import br.ufpe.cin.timetracker.model.TimeIntervalModel

@Database(entities = [TaskModel::class, TimeIntervalModel::class], version = 1)
@TypeConverters(Converters::class)
abstract  class TaskDB : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO

    companion object {
        @Volatile
        private var INSTANCE: TaskDB? = null
        fun getInstance(applicationContext: Context): TaskDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = Room.databaseBuilder(
                        applicationContext,
                        TaskDB::class.java,
                        "tasks.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
