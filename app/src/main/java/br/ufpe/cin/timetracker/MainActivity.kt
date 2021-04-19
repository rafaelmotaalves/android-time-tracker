package br.ufpe.cin.timetracker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.ufpe.cin.timetracker.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        scheduleNotificationService()

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

    private fun scheduleNotificationService() {
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val frequency: Long = 60 * 1000
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(), frequency, pendingIntent)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification channel"
            val descriptionText = "Notification channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationService.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}