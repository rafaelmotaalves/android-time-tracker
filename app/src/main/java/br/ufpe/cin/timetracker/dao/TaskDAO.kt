package br.ufpe.cin.timetracker.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.ufpe.cin.timetracker.model.TaskModel
import br.ufpe.cin.timetracker.model.TaskWithTimeIntervalsModel
import br.ufpe.cin.timetracker.model.TimeIntervalModel

@Dao
interface TaskDAO {

    @Transaction
    @Query("select * from tasks order by done ASC, name ASC")
    fun getTasksWithIntervals(): LiveData<List<TaskWithTimeIntervalsModel>>

    @Update
    suspend fun updateTimeInterval(timeIntervalModel: TimeIntervalModel)

    @Insert
    suspend fun insertTimeInterval(timeIntervalModel: TimeIntervalModel)

    @Update
    suspend fun updateTask(task: TaskModel)
}