package br.ufpe.cin.timetracker

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.ufpe.cin.timetracker.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val tabs = listOf<Fragment>(
            TasksFragment.newInstance(viewModel, TasksMode.CURRENT),
            TasksFragment.newInstance(viewModel, TasksMode.HISTORY)
        )

        val adapter = TabsAdapter(this, tabs)
        binding.viewpager.adapter = adapter

        val tabNames = listOf("Tasks", "History")
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        binding.floatingActionButton.setOnClickListener {
            CreateTaskDialog.display(supportFragmentManager)
        }
    }
}