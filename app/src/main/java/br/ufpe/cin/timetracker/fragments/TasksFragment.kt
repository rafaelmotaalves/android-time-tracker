package br.ufpe.cin.timetracker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpe.cin.timetracker.adapters.TaskAdapter
import br.ufpe.cin.timetracker.databinding.FragmentTasksBinding
import br.ufpe.cin.timetracker.util.PermissionsHelper
import br.ufpe.cin.timetracker.viewmodels.TaskTimerViewModel

enum class TasksMode {
    CURRENT, HISTORY
}

class TasksFragment (private val permissionsHelper: PermissionsHelper, private val taskTimerViewModel: TaskTimerViewModel, private val mode: TasksMode): Fragment() {
    private lateinit var binding: FragmentTasksBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)

        val taskAdapter = TaskAdapter(permissionsHelper, taskTimerViewModel, activity as Context, layoutInflater)

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = taskAdapter
        }
        binding.rvTasks.addItemDecoration(DividerItemDecoration(binding.rvTasks.context, DividerItemDecoration.VERTICAL))

        when(mode) {
            TasksMode.CURRENT ->
                taskTimerViewModel.tasks.observe(viewLifecycleOwner, {
                    taskAdapter.submitList(it)
                })
            TasksMode.HISTORY ->
                taskTimerViewModel.historyTasks.observe(viewLifecycleOwner, {
                    taskAdapter.submitList(it)
                })
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        taskTimerViewModel.startBackgroundTimerUpdater()
    }

    override fun onPause() {
        taskTimerViewModel.stopBackgroundTimerUpdater()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            permissionsHelper: PermissionsHelper,
            taskTimerViewModel: TaskTimerViewModel,
            mode: TasksMode) = TasksFragment(permissionsHelper, taskTimerViewModel, mode)
    }
}