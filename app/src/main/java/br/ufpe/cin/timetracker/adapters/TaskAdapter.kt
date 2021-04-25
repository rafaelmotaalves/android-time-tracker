package br.ufpe.cin.timetracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.ufpe.cin.timetracker.TaskViewHolder
import br.ufpe.cin.timetracker.viewmodels.TaskTimerViewModel
import br.ufpe.cin.timetracker.databinding.TaskBinding
import br.ufpe.cin.timetracker.dto.Task
import br.ufpe.cin.timetracker.util.PermissionsHelper

class TaskAdapter(
    private val permissionsHelper: PermissionsHelper,
    private val taskTimerViewModel: TaskTimerViewModel,
    private val context: Context,
    private val inflater: LayoutInflater
    ) : ListAdapter<Task, TaskViewHolder>(TaskDiffer) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskBinding.inflate(inflater)

        return TaskViewHolder(permissionsHelper, taskTimerViewModel, context, binding)
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