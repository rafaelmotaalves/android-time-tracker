package br.ufpe.cin.timetracker.repo.task

import br.ufpe.cin.timetracker.model.Task
import br.ufpe.cin.timetracker.model.TimeInterval
import java.time.Instant
import java.util.stream.Collectors

class TaskInMemoryRepository : TaskRepository {

    private var timeIntervalLastId = 2

    override val tasks = arrayListOf(
        Task(1, "Test 1", "Description 1"),
        Task(2, "Test 2", "Description 2")
    )

    override val timeIntervals = arrayListOf(
        TimeInterval(1, Instant.parse("2021-03-25T10:00:00.00Z"), Instant.parse("2021-03-25T11:00:00.00Z"), 1)
    )

    override fun getTimeIntervals(taskId: Int): List<TimeInterval> {
        return timeIntervals.stream()
            .sorted { a, b -> a.id - b.id }
            .filter { it.taskId == taskId }
            .collect(Collectors.toList())
    }

    override fun updateTimeInterval(timeIntervalId: Int, end: Instant) {
        val timeInterval = timeIntervals.stream()
            .filter { it.id == timeIntervalId }
            .findFirst()
            .orElse(null)

        timeInterval.end = end
    }
    override fun insertTimeInterval(taskId: Int, start: Instant) {
        timeIntervals.add(TimeInterval(++timeIntervalLastId, start, null, taskId))
    }

}