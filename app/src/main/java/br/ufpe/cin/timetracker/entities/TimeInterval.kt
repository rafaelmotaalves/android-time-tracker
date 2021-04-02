package br.ufpe.cin.timetracker.entities

import java.time.Duration
import java.time.Instant

class TimeInterval (
        val id: Int = 0,
        val start: Instant,
        var end: Instant? = null,
        val taskId: Int = 0
        ) {

        fun getElapsedTime(): Long =
                if (end != null)
                        getTimeInSecondsBetweenInstants(start, end!!)
                        else getTimeInSecondsBetweenInstants(start, Instant.now())

        private fun getTimeInSecondsBetweenInstants(start: Instant, end: Instant) : Long =
                Duration.between(start, end).seconds
}