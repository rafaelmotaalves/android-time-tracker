package br.ufpe.cin.timetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.model.Task

class TaskAdapter(private val viewModel: TaskViewModel, private val lifecycleOwner: LifecycleOwner, private val inflater: LayoutInflater) : ListAdapter<Task, TaskViewHolder>(TaskDiffer) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskBinding.inflate(inflater)

        return TaskViewHolder(viewModel, lifecycleOwner, binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    private object TaskDiffer : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.name == newItem.name && oldItem.description == newItem.description
        }

    }
}