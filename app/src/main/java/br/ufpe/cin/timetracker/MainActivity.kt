package br.ufpe.cin.timetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.ufpe.cin.timetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TimeTrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.elapsedTime.observe (this, Observer {
            binding.content.text = it
        })

        binding.start.setOnClickListener {
            viewModel.startTimer()
        }

        binding.stop.setOnClickListener {
            viewModel.stopTimer()
        }
    }
}