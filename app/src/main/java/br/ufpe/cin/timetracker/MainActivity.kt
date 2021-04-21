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
import br.ufpe.cin.timetracker.adapters.TabsAdapter
import br.ufpe.cin.timetracker.databinding.ActivityMainBinding
import br.ufpe.cin.timetracker.fragments.StatisticsFragment
import br.ufpe.cin.timetracker.services.NotificationService
import br.ufpe.cin.timetracker.util.PermissionsHelper
import br.ufpe.cin.timetracker.viewmodels.StatisticsViewModel
import br.ufpe.cin.timetracker.viewmodels.TaskTimerViewModel
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val taskTimerViewModel: TaskTimerViewModel by viewModels()
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    private val permissionsHelper: PermissionsHelper = PermissionsHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        scheduleNotificationService()
        if (!permissionsHelper.hasPermissions()) {
            permissionsHelper.requestPermissions()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val tabs = listOf<Fragment>(
            TasksFragment.newInstance(permissionsHelper, taskTimerViewModel, TasksMode.CURRENT),
            TasksFragment.newInstance(permissionsHelper, taskTimerViewModel, TasksMode.HISTORY),
            StatisticsFragment.newInstance(permissionsHelper, statisticsViewModel)
        )

        val adapter = TabsAdapter(this, tabs)
        binding.viewpager.adapter = adapter
        binding.viewpager.isUserInputEnabled = false

        val tabNames = listOf("Tasks", "History", "Statistics")
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        binding.floatingActionButton.setOnClickListener {
            CreateTaskDialog.display(supportFragmentManager, application)
        }
    }

    private fun scheduleNotificationService() {
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val frequency: Long = 60 * 1000
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            frequency,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification channel"
            val descriptionText = "Notification channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NotificationService.CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}