package br.ufpe.cin.timetracker.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("taskId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TimeInterval(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val start: Instant,
    var end: Instant?,
    val taskId: Int
)