package br.ufpe.cin.timetracker.mappers

import br.ufpe.cin.timetracker.dto.TaskUserInput
import br.ufpe.cin.timetracker.model.TaskModel

class TaskModelMapper {
    companion object {
        fun fromUserInput(taskUserInput: TaskUserInput) : TaskModel {
            return TaskModel(
                name = taskUserInput.name,
                description = taskUserInput.description,
                estimateInSeconds = toSeconds(taskUserInput.hour, taskUserInput.minute)
            )
        }

        private fun toSeconds(hour: Int, minute: Int) : Long {
            return (hour.toLong() * 60 * 60) + (minute.toLong() * 60)
        }
    }
}