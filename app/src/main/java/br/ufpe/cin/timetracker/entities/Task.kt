package br.ufpe.cin.timetracker.entities

import androidx.lifecycle.MutableLiveData

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE, PAUSED
}

class Task (
        val id: Int = 0,
        val name: String,
        val intervals: List<TimeInterval>,
        val description: String,
        var done: Boolean,
        val estimate: Long
) : Comparable<Task> {
    val status: TaskStatus
        get() = when {
            done -> TaskStatus.DONE
            active -> TaskStatus.IN_PROGRESS
            intervals.isNotEmpty() -> TaskStatus.PAUSED
            else -> TaskStatus.TODO
        }

    val active: Boolean
        get() = intervals.isNotEmpty() && intervals.last().end == null

    val late: Boolean
        get() = (elapsedTime.value ?: 0) > estimate

    val elapsedTime: MutableLiveData<Long> = MutableLiveData(calculateElapsedTime())

    fun updateElapsedTime() {
        elapsedTime.postValue(calculateElapsedTime())
    }

    private fun calculateElapsedTime(): Long =
        intervals.stream().mapToLong {
            it.getElapsedTime()
        }.sum()

    override fun compareTo(other: Task): Int {
        return if (other.status == status) {
            name.compareTo(other.name)
        } else {
            when(status) {
                TaskStatus.TODO -> 0
                TaskStatus.IN_PROGRESS -> -1
                TaskStatus.PAUSED -> -1
                TaskStatus.DONE -> 1
            }
        }
    }
}