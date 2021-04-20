package br.ufpe.cin.timetracker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpe.cin.timetracker.databinding.FragmentTasksBinding
import br.ufpe.cin.timetracker.util.PermissionsHelper

enum class TasksMode {
    CURRENT, HISTORY
}

class TasksFragment (private val permissionsHelper: PermissionsHelper, private val viewModel: TaskViewModel, private val mode: TasksMode): Fragment() {
    private lateinit var binding: FragmentTasksBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(layoutInflater)

        val taskAdapter = TaskAdapter(permissionsHelper, viewModel, activity as Context, layoutInflater)

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = taskAdapter
        }
        binding.rvTasks.addItemDecoration(DividerItemDecoration(binding.rvTasks.context, DividerItemDecoration.VERTICAL))

        when(mode) {
            TasksMode.CURRENT ->
                viewModel.tasks.observe(viewLifecycleOwner, {
                    taskAdapter.submitList(it)
                })
            TasksMode.HISTORY ->
                viewModel.historyTasks.observe(viewLifecycleOwner, {
                    taskAdapter.submitList(it)
                })
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.startBackgroundTimerUpdater()
    }

    override fun onPause() {
        viewModel.stopBackgroundTimerUpdater()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            permissionsHelper: PermissionsHelper,
            viewModel: TaskViewModel,
            mode: TasksMode) = TasksFragment(permissionsHelper, viewModel, mode)
    }
}