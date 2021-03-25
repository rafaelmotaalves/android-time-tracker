package br.ufpe.cin.timetracker.model

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var description: String
) {
    @Ignore
    val elapsedTime = MutableLiveData<String>()
}