package br.ufpe.cin.timetracker.model

import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TimeInterval
import java.util.stream.Collectors

fun TaskWithTimeIntervalsModel.toTask() = Task(
        id = task.id,
        name = task.name,
        intervals = intervals.stream().map {
            it.toTimeInterval()
        }.collect(Collectors.toList()),
        done = task.done,
        notified = task.notified,
        description = task.description,
        estimate = task.estimateInSeconds
)

fun TimeIntervalModel.toTimeInterval() = TimeInterval(
        id = id,
        start = start,
        end = end,
        taskId = taskId
)

fun TimeInterval.toTimeIntervalModel() = TimeIntervalModel(
        id = id,
        start = start,
        end = end,
        taskId = taskId
)


fun Task.toTaskModel() = TaskModel(
        id = id,
        description = description,
        name = name,
        done = done,
        notified = notified,
        estimateInSeconds = estimate
)