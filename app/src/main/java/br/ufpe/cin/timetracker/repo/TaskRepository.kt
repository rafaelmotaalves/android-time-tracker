package br.ufpe.cin.timetracker.repo

import androidx.lifecycle.Transformations
import br.ufpe.cin.timetracker.dao.TaskDAO
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TimeInterval
import br.ufpe.cin.timetracker.model.TimeIntervalModel
import java.util.stream.Collectors

class TaskRepository (private val dao: TaskDAO)  {


    val tasks = Transformations.map(dao.getTasksWithIntervals()) { tasks ->
        tasks.stream().map { task ->
            val intervals = task.intervals.stream().map {
                interval -> TimeInterval(interval.id, interval.start, interval.end)
            }.collect(Collectors.toList())

            Task(task.task.id, task.task.name, intervals, task.task.done)
        }.collect(Collectors.toList())
    }

    suspend fun updateTimeInterval(task: Task, timeInterval: TimeInterval) {
        val model = TimeIntervalModel(timeInterval.id, timeInterval.start, timeInterval.end, task.id)

        dao.updateTimeInterval(model)
    }

    suspend fun insertTimeInterval(task: Task, timeInterval: TimeInterval) {
        val model = TimeIntervalModel(start = timeInterval.start, end = timeInterval.end, taskId = task.id)

        dao.insertTimeInterval(model)
    }

}