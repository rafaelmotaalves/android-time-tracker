package br.ufpe.cin.timetracker.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithTimeIntervalsModel (
        @Embedded val task: TaskModel,
        @Relation(
            parentColumn = "id",
                entityColumn = "taskId"
        )
        val intervals: List<TimeIntervalModel>

        )