package br.ufpe.cin.timetracker.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "time_intervals")
data class TimeIntervalModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val start: Instant,
    var end: Instant? = null,
    val taskId: Int
)