package br.ufpe.cin.timetracker

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.entities.TaskStatus

class TaskViewHolder(private val viewModel: TaskViewModel, private val context: Context, private val binding: TaskBinding) : RecyclerView.ViewHolder(binding.root) {

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

    private fun alertDialog(task: Task): AlertDialog {
        var optionsAlert: AlertDialog? = null
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
                        0 -> viewModel.concludeTask(task)
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