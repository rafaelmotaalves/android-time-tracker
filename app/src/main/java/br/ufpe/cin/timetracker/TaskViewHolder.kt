package br.ufpe.cin.timetracker

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.model.Task

class TaskViewHolder(private val viewModel: TaskViewModel, private val lifecycleOwner: LifecycleOwner, private val binding: TaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(task: Task) {

        binding.name.text = task.name

        task.elapsedTime.observe(lifecycleOwner,
            Observer {
                binding.timer.text = it
            }
        )

        binding.root.setOnClickListener {
            viewModel.startTimer(task.id)
        }
    }
}