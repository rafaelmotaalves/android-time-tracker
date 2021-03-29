package br.ufpe.cin.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpe.cin.timetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskAdapter = TaskAdapter(viewModel,this, layoutInflater)

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = taskAdapter
        }
        binding.rvTasks.addItemDecoration(DividerItemDecoration(binding.rvTasks.context, DividerItemDecoration.VERTICAL))

        viewModel.tasks.observe(this, {
            taskAdapter.submitList(it)
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.startBackgroundTimerUpdater()
    }

    override fun onPause() {
        viewModel.stopBackgroundTimerUpdater()
        super.onPause()
    }
}