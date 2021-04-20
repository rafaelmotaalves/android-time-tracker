package br.ufpe.cin.timetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.util.PermissionsHelper

class TaskAdapter(
    private val permissionsHelper: PermissionsHelper,
    private val viewModel: TaskViewModel,
    private val context: Context,
    private val inflater: LayoutInflater
    ) : ListAdapter<Task, TaskViewHolder>(TaskDiffer) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskBinding.inflate(inflater)

        return TaskViewHolder(permissionsHelper, viewModel, context, binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    private object TaskDiffer : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.done == newItem.done &&
                    oldItem.intervals == newItem.intervals
        }

    }
}