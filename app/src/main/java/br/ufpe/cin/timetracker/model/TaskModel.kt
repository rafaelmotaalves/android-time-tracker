package br.ufpe.cin.timetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var description: String,
    var estimateInSeconds: Long,
    val done: Boolean = false
) {
}