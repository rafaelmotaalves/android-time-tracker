package br.ufpe.cin.timetracker.entities

import androidx.lifecycle.MutableLiveData

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

class Task (
        val id: Int = 0,
        val name: String,
        val intervals: List<TimeInterval>,
        val done: Boolean,
        val estimate: Long
) {
    val status: TaskStatus
        get() = when {
            done -> TaskStatus.DONE
            intervals.isNotEmpty() -> TaskStatus.IN_PROGRESS
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
}