package br.ufpe.cin.timetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.ufpe.cin.timetracker.dao.TaskDB
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TimeInterval
import br.ufpe.cin.timetracker.repo.TaskRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class TaskViewModel(application: Application) :
        AndroidViewModel(application) {

    companion object {
        const val POOLING_TIMEOUT: Long = 1000
    }

    private val repo: TaskRepository = TaskRepository(TaskDB.getInstance(application.applicationContext).taskDAO())
    private var timerTask: TimerTask? = null

    val tasks = repo.activeTasks
    val historyTasks = repo.historyTasks

    fun startBackgroundTimerUpdater() {
        timerTask = Timer().scheduleAtFixedRate(0, POOLING_TIMEOUT) {
            tasks.value?.forEach {
                it.updateElapsedTime()
            }
        }
    }

    fun stopBackgroundTimerUpdater() {
        timerTask?.cancel()
    }

    fun toggleTimer(task: Task) {
        if (task.active) {
            stopTimer(task)
        } else {
            startTimer(task)
        }
    }

    fun concludeTask(task: Task) {
        if (!task.done) {
            stopTimer(task)
            task.done = true
            viewModelScope.launch {
                repo.updateTask(task)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.deleteTask(task)
        }
    }

    private fun startTimer(task: Task) {
        if (!task.active && !task.done) {
            viewModelScope.launch {
                val startInstant = Instant.now()

                repo.insertTimeInterval(TimeInterval(start = startInstant, taskId = task.id))
            }
        }
    }

    private fun stopTimer(task: Task) {
        val endInstant = Instant.now()

        if (task.active && !task.done) {
            val lastInterval = task.intervals.last()

            viewModelScope.launch {
                repo.updateTimeInterval(TimeInterval(lastInterval.id, lastInterval.start, endInstant, task.id))
            }
        }
    }
}