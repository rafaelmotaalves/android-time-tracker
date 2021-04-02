package br.ufpe.cin.timetracker

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.entities.Task

class TaskViewHolder(private val viewModel: TaskViewModel, private val lifecycleOwner: LifecycleOwner, private val binding: TaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(task: Task) {

        binding.name.text = task.name

        task.elapsedTime.observe(lifecycleOwner,
            Observer {
                binding.timer.text = formatElapsedTimeString(it)
            }
        )

        binding.root.setOnClickListener {
            viewModel.toggleTimer(task)
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
}