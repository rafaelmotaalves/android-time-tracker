package br.ufpe.cin.timetracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TaskSearchViewModel(application: Application) : AndroidViewModel(application) {
    private var searchedTask : String = ""

    fun setSearchedTask(taskName : String) {
        searchedTask = taskName
    }

    fun getSearchedTask() : String {
        return searchedTask
    }
}