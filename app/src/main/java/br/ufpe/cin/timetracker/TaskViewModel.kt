package br.ufpe.cin.timetracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.ufpe.cin.timetracker.model.Task
import br.ufpe.cin.timetracker.model.TimeInterval
import br.ufpe.cin.timetracker.repo.task.TaskInMemoryRepository
import br.ufpe.cin.timetracker.repo.task.TaskRepository
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class TaskViewModel(application: Application) :
    AndroidViewModel(application) {

    companion object {
        const val POOLING_TIMEOUT: Long = 1000
    }

    private val repo: TaskRepository = TaskInMemoryRepository()
    private var timerTask: TimerTask? = null

    val tasks = MutableLiveData(repo.tasks)

    fun startBackgroundTimerUpdater() {
        timerTask = Timer().scheduleAtFixedRate(0, POOLING_TIMEOUT) {
            repo.tasks.forEach {
                val timeIntervals = repo.getTimeIntervals(it.id)

                val elapsedSeconds = getElapsedTime(timeIntervals)

                it.elapsedTime.postValue(formatElapsedTimeString(elapsedSeconds))
            }
        }
    }

    fun stopBackgroundTimerUpdater() {
        timerTask?.cancel()
    }

    fun startTimer(taskId: Int) {
            val timeIntervals = repo.getTimeIntervals(taskId)

            if (timeIntervals.isEmpty() || timeIntervals.last().end != null) {
                val startInstant = Instant.now()

                repo.insertTimeInterval(taskId, startInstant)
            }
    }

    fun stopTimer(taskId: Int) {
        val endInstant = Instant.now()
        val timeIntervals = repo.getTimeIntervals(taskId)

        val lastInterval = timeIntervals.last()
        if (lastInterval.end == null) {
            repo.updateTimeInterval(lastInterval.id, endInstant)
        }
    }

    private fun formatElapsedTimeString(elapsedTime: Long): String {
        val totalMinutes = elapsedTime.div(60)

        val minutes = totalMinutes % 60
        val hours = totalMinutes.div(60)
        val seconds = elapsedTime % 60

        return "${formatNumeral(hours)}:${formatNumeral(minutes)}:${formatNumeral(seconds)}"
    }

    private fun formatNumeral (num: Long): String = num.toString().padStart(2, '0')

    private fun getElapsedTime(timeIntervals: List<TimeInterval>): Long {
        return timeIntervals.stream().mapToLong {
            if (it.end != null) {
                getTimeInSecondsBetweenInstants(it.start, it.end!!)
            } else {
                getTimeInSecondsBetweenInstants(it.start, Instant.now())
            }
        }.sum()
    }

    private fun getTimeInSecondsBetweenInstants(start: Instant, end: Instant) : Long = Duration.between(start, end).seconds
}