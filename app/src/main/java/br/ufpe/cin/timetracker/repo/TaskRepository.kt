package br.ufpe.cin.timetracker.repo

import androidx.lifecycle.Transformations
import br.ufpe.cin.timetracker.dao.TaskDAO
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TimeInterval
import br.ufpe.cin.timetracker.model.*
import java.util.stream.Collectors

class TaskRepository (private val dao: TaskDAO)  {

    val activeTasks = Transformations.map(dao.getTasksWithIntervals()) { tasks ->
        tasks.stream().map { it.toTask() }.filter{ !it.done }.sorted().collect(Collectors.toList())
    }

    val historyTasks = Transformations.map(dao.getTasksWithIntervals()) { tasks ->
        tasks.stream().map { it.toTask() }.filter{ it.done }.sorted().collect(Collectors.toList())
    }

    suspend fun updateTimeInterval(timeInterval: TimeInterval) =
        dao.updateTimeInterval(timeInterval.toTimeIntervalModel())

    suspend fun insertTimeInterval(timeInterval: TimeInterval) =
        dao.insertTimeInterval(timeInterval.toTimeIntervalModel())

    suspend fun updateTask(task: Task) =
        dao.updateTask(task.toTaskModel())

    suspend fun deleteTask(task: Task) =
        dao.deleteTask(task.toTaskModel())

    suspend fun insertTask(task: TaskModel) =
            dao.insertTask(task)
}