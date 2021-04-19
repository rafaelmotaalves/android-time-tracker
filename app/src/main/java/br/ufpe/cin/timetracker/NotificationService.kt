package br.ufpe.cin.timetracker

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.ufpe.cin.timetracker.dao.TaskDB
import br.ufpe.cin.timetracker.entities.Task
import br.ufpe.cin.timetracker.repo.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService : Service() {
    companion object {
        const val CHANNEL_ID = "br.ufpe.cin.timetracker.NotificationService"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var repo: TaskRepository

    private fun createNotification(task: Task): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_event_note_24)
            .setContentTitle(getString(R.string.notification_task_exceeded_title))
            .setContentText(getString(R.string.notification_task_exceeded_description, task.name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        repo = TaskRepository(TaskDB.getInstance(applicationContext).taskDAO())
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(this.javaClass.simpleName, "Starting notification worker")

        coroutineScope.launch {
            repo.getActiveTasks().forEach { task ->
                task.updateElapsedTime()

                Log.d(this.javaClass.simpleName, "Checking task " + task.name)
                // TODO: Add the logic for controlling notification repetition
                if (task.late) {
                    val notification = createNotification(task)
                    val notificationManager = NotificationManagerCompat.from(applicationContext)
                    notificationManager.notify(task.id, notification)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}