package br.ufpe.cin.timetracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class TimeTrackerViewModel(application: Application) :
    AndroidViewModel(application) {

    companion object {
        const val POOLING_TIMEOUT: Long = 1000
    }

    val elapsedTime = MutableLiveData<String>()
    private var timerTask: TimerTask? = null

    fun startTimer() {
        val startInstant = Instant.now()
        Log.d(this.javaClass.simpleName, "Starting timer")

        timerTask = Timer().scheduleAtFixedRate(0, POOLING_TIMEOUT) {
            val elapsedSeconds = getElapsedTimeInSeconds(startInstant)
            elapsedTime.postValue(formatElapsedTimeString(elapsedSeconds))
        }
    }

    fun stopTimer() {
        Log.d(this.javaClass.simpleName, "Stopping timer")
        timerTask?.cancel()
    }

    private fun formatElapsedTimeString(elapsedTime: Long): String {
        val minutes = elapsedTime.div(60)
        val seconds = elapsedTime % 60

        return "${formatNumeral(minutes)}:${formatNumeral(seconds)}"
    }

    private fun formatNumeral (num: Long): String = num.toString().padStart(2, '0')

    private fun getElapsedTimeInSeconds(startInstant: Instant) : Long = Duration.between(startInstant, Instant.now()).seconds
}