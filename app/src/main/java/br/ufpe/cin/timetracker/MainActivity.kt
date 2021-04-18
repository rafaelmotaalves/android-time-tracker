package br.ufpe.cin.timetracker

import android.R
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.ufpe.cin.timetracker.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabs = listOf<Fragment>(
            TasksFragment.newInstance(viewModel, TasksMode.CURRENT),
            TasksFragment.newInstance(viewModel, TasksMode.HISTORY)
        )
        supportActionBar?.elevation = .0F;

        val adapter = TabsAdapter(this, tabs)
        binding.viewpager.adapter = adapter

        val tabNames = listOf("Current", "History")
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab ,position ->
            tab.text = tabNames[position]
        }.attach()
    }

}