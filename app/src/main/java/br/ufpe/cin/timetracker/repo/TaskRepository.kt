package br.ufpe.cin.timetracker.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import br.ufpe.cin.timetracker.dao.TaskDAO
import br.ufpe.cin.timetracker.dto.Task
import br.ufpe.cin.timetracker.dto.TimeInterval
import br.ufpe.cin.timetracker.model.*
import java.util.stream.Collectors


class TaskRepository(private val dao: TaskDAO)  {
    var filterNameActiveTasks = MutableLiveData<String>()
    var filterNameHistoryTasks = MutableLiveData<String>()

    init {
        filterNameActiveTasks.value = ""
        filterNameHistoryTasks.value = ""
    }

    val activeTasks = Transformations.switchMap(filterNameActiveTasks) { input ->
        if (input.isBlank()) {
            mapLiveDataToTaskDto(dao.getTasksWithIntervals(done=false))
        } else {
            mapLiveDataToTaskDto(dao.getTasksWithIntervals(done = false, nameLike="$input%"))
        }
    }

    val historyTasks = Transformations.switchMap(filterNameHistoryTasks) { input ->
        if (input.isBlank()) {
            mapLiveDataToTaskDto(dao.getTasksWithIntervals(done=true))
        } else {
            mapLiveDataToTaskDto(dao.getTasksWithIntervals(done=true, nameLike="$input%"))
        }
    }

    val historyTasksWithLocation = Transformations.map(historyTasks) { tasks -> tasks.stream().filter { it.doneLocation != null }.collect(
        Collectors.toList()
    ) }

    private fun mapLiveDataToTaskDto(data: LiveData<List<TaskWithTimeIntervalsModel>>): LiveData<MutableList<Task>> {
        return Transformations.map(data) { tasks ->
            tasks.stream().map { it.toTask() }.sorted().collect(Collectors.toList())
        }
    }

    suspend fun getActiveTasks(): MutableList<Task> =
        dao.getActiveTasks().stream().map { it.toTask() }.collect(Collectors.toList())

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