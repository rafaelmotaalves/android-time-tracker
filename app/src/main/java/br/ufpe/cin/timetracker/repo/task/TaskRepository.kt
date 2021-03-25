package br.ufpe.cin.timetracker.repo.task

import br.ufpe.cin.timetracker.model.Task
import br.ufpe.cin.timetracker.model.TimeInterval
import java.time.Instant

interface TaskRepository {
    val tasks : List<Task>
    val timeIntervals : List<TimeInterval>

    fun getTimeIntervals(taskId: Int) : List<TimeInterval>
    fun updateTimeInterval(intervalId: Int, end: Instant)
    fun insertTimeInterval(taskId: Int, start: Instant)
}