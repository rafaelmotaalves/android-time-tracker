package br.ufpe.cin.timetracker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.entities.Location
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TaskStatus
import br.ufpe.cin.timetracker.util.PermissionsHelper
import br.ufpe.cin.timetracker.viewmodels.TaskTimerViewModel
import com.google.android.gms.location.LocationServices

class TaskViewHolder(
    private val permissionsHelper: PermissionsHelper,
    private val taskTimerViewModel: TaskTimerViewModel,
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
            taskTimerViewModel.toggleTimer(task)
        }

        var optionsAlert: AlertDialog = alertDialog(task)

        binding.root.setOnLongClickListener {
            optionsAlert.show()
            true
        }
    }

    @SuppressLint("MissingPermission")
    private fun concludeTask(task: Task) {
        if (permissionsHelper.hasPermissions()) {
            LocationServices.getFusedLocationProviderClient(context)
                .lastLocation.addOnSuccessListener {
                    val doneLocation = Location(it.latitude, it.longitude)

                    taskTimerViewModel.concludeTask(task, doneLocation)
                }.addOnFailureListener {
                    taskTimerViewModel.concludeTask(task)
                }
        } else {
            taskTimerViewModel.concludeTask(task)
        }
    }

    private fun alertDialog(task: Task): AlertDialog {
        var optionsAlert: AlertDialog?
        if (task.done) {
            optionsAlert = AlertDialog.Builder(context)
                .setItems(
                    R.array.actions_without_done
                ) { _, which ->
                    when (which) {
                        0 -> taskTimerViewModel.deleteTask(task)
                        else -> true
                    }
                }.create()
        } else {
            optionsAlert = AlertDialog.Builder(context)
                .setItems(R.array.actions) { _, which ->
                    when (which) {
                        0 -> concludeTask(task)
                        1 -> taskTimerViewModel.deleteTask(task)
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