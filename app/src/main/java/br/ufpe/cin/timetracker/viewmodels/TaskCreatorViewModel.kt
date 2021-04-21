package br.ufpe.cin.timetracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.ufpe.cin.timetracker.dao.TaskDB
import br.ufpe.cin.timetracker.model.TaskModel
import br.ufpe.cin.timetracker.repo.TaskRepository
import kotlinx.coroutines.launch

class TaskCreatorViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: TaskRepository = TaskRepository(TaskDB.getInstance(application.applicationContext).taskDAO())

    fun createTask(taskModel: TaskModel) {
        viewModelScope.launch {
            repo.insertTask(taskModel)
        }
    }
}