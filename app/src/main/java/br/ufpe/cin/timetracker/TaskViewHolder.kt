package br.ufpe.cin.timetracker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.location.Criteria
import android.location.LocationManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.entities.Location
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TaskStatus
import br.ufpe.cin.timetracker.util.PermissionsHelper
import java.security.Provider

class TaskViewHolder(
    private val permissionsHelper: PermissionsHelper,
    private val viewModel: TaskViewModel,
    private val context: Context,
    private val binding: TaskBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(task: Task) {
        binding.name.text = task.name

        task.elapsedTime.observe(context as LifecycleOwner,
            Observer {
                binding.timer.text = formatElapsedTimeString(it)

                val status = getStatusString(task.status)
                val late = getIsLateString(task.late)
                binding.status.text = "$status $late"
            }
        )

        binding.root.setOnClickListener {
            viewModel.toggleTimer(task)
        }

        var optionsAlert: AlertDialog = alertDialog(task)

        binding.root.setOnLongClickListener {
            optionsAlert.show()
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun concludeTask(task: Task) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (permissionsHelper.hasPermissions() && locationManager.isLocationEnabled) {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val bestProvider = locationManager.getBestProvider(criteria, true)
            val location = locationManager.getLastKnownLocation(bestProvider ?: LocationManager.GPS_PROVIDER)
            if (location != null) {
                val doneLocation = Location(location.latitude, location.longitude)

                return viewModel.concludeTask(task, doneLocation)
            }
        }

        return viewModel.concludeTask(task)
    }

    private fun alertDialog(task: Task): AlertDialog {
        var optionsAlert: AlertDialog?
        if (task.done) {
            optionsAlert = AlertDialog.Builder(context)
                .setItems(
                    R.array.actions_without_done
                ) { _, which ->
                    when (which) {
                        0 -> viewModel.deleteTask(task)
                        else -> true
                    }
                }.create()
        } else {
            optionsAlert = AlertDialog.Builder(context)
                .setItems(R.array.actions) { _, which ->
                    when (which) {
                        0 -> concludeTask(task)
                        1 -> viewModel.deleteTask(task)
                        else -> true
                    }
                }.create()
        }
        return optionsAlert
    }

    private fun getIsLateString(late: Boolean) = if (late) context.getString(R.string.status_late) else ""

    private fun getStatusString(status: TaskStatus) = when(status) {
        TaskStatus.TODO -> context.getString(R.string.status_todo)
        TaskStatus.IN_PROGRESS -> context.getString(R.string.status_in_progress)
        TaskStatus.DONE -> context.getString(R.string.status_done)
        TaskStatus.PAUSED -> context.getString(R.string.status_paused)
    }

    private fun formatElapsedTimeString(elapsedTime: Long): String {
        val totalMinutes = elapsedTime.div(60)

        val minutes = totalMinutes % 60
        val hours = totalMinutes.div(60)
        val seconds = elapsedTime % 60

        return "${formatNumeral(hours)}:${formatNumeral(minutes)}:${formatNumeral(seconds)}"
    }

    private fun formatNumeral (num: Long): String = num.toString().padStart(2, '0')
}