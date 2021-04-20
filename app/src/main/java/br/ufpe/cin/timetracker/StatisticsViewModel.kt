package br.ufpe.cin.timetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.ufpe.cin.timetracker.dao.TaskDB
import br.ufpe.cin.timetracker.repo.TaskRepository

class StatisticsViewModel (application: Application) : AndroidViewModel(application) {
    private val repo: TaskRepository = TaskRepository(TaskDB.getInstance(application.applicationContext).taskDAO())

    val tasks = repo.historyTasksWithLocation
}