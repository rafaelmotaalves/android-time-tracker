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

    private var selectedHour : Int = 0
    private var selectedMinute: Int = 1

    fun setSelectedHour(hour : Int) {
        selectedHour = hour
    }

    fun setSelectedMinute(minute : Int) {
        selectedMinute = minute
    }

    fun getSelectedHour() : Int {
        return selectedHour
    }

    fun getSelectedMinute() : Int {
        return selectedMinute
    }

    fun createTask(taskModel: TaskModel) {
        viewModelScope.launch {
            repo.insertTask(taskModel)
        }
    }
}