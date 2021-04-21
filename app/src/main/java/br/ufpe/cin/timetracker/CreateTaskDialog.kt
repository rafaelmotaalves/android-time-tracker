package br.ufpe.cin.timetracker

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.ufpe.cin.timetracker.dao.TaskDB
import br.ufpe.cin.timetracker.dto.TaskUserInput
import br.ufpe.cin.timetracker.mappers.TaskModelMapper
import br.ufpe.cin.timetracker.repo.TaskRepository
import br.ufpe.cin.timetracker.viewmodels.TaskCreatorViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat


class CreateTaskDialog(private val taskCreatorViewModel: TaskCreatorViewModel) : DialogFragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var title: TextInputLayout
    private lateinit var description: TextInputLayout
    private lateinit var timePicker : MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.create_task, container, false)

        toolbar = view.findViewById(R.id.toolbar)
        title = view.findViewById(R.id.taskTitleField)
        description = view.findViewById(R.id.taskDescField)

        val timePickerButton: Button = view.findViewById(R.id.timePickerButton)

        configureTimePicker(timePickerButton)

        return view
    }

    private fun configureTimePicker(timePickerButton: Button) {
        timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setHour(0)
                .setMinute(1)
                .setTitleText("Select estimated time")
                .build()

        timePickerButton.setOnClickListener {
            timePicker.show(childFragmentManager, TAG_TIMER_PICKER)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Create new task"
        toolbar.inflateMenu(R.menu.create_task)
        toolbar.setOnMenuItemClickListener {
            val title = title.editText?.text.toString()
            val description = description.editText?.text.toString()
            val input = TaskUserInput(title, description, timePicker.hour, timePicker.minute)

            taskCreatorViewModel.createTask(TaskModelMapper.fromUserInput(input))

            dismiss()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.Theme_TimeTracker_Slide)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_TimeTracker_FullScreenDialog)
    }

    companion object {
        private const val TAG = "create_task_dialog"
        private const val TAG_TIMER_PICKER = "timer_picker_dialog"
        fun display(fragmentManager: FragmentManager, application: Application): CreateTaskDialog {
            val createTaskDialog = CreateTaskDialog(TaskCreatorViewModel(application))
            createTaskDialog.show(fragmentManager, TAG)
            return createTaskDialog
        }
    }
}