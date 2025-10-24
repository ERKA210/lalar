package com.example.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.provider.CalendarContract
import androidx.appcompat.widget.SwitchCompat
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var taskTitle: EditText
    private lateinit var taskDate: TextView
    private lateinit var startTime: TextView
    private lateinit var reminderSwitch: SwitchCompat
    private lateinit var createButton: Button
    private lateinit var backButton: ImageButton

    private val calendar = Calendar.getInstance()
    private var selectedCategory: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        initializeViews()
        setupDatePicker()
        setupTimePicker()
        setupCreateButton()
        setupBackButton()
        setupCategoryButtons()
    }

    private fun initializeViews() {
        taskTitle = findViewById(R.id.editTextTaskName)
        taskDate = findViewById(R.id.editTextDate)
        startTime = findViewById(R.id.editTextTime)
        reminderSwitch = findViewById(R.id.mySwitch)
        createButton = findViewById(R.id.createButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupCategoryButtons() {
        val lifeButton: Button = findViewById(R.id.life_button)
        val workButton: Button = findViewById(R.id.work_button)
        val studyButton: Button = findViewById(R.id.study_button)
        val eventButton: Button = findViewById(R.id.event_button)

        lifeButton.setOnClickListener {
            selectedCategory = "Life"
            updateCategorySelection(lifeButton)
        }
        workButton.setOnClickListener {
            selectedCategory = "Work"
            updateCategorySelection(workButton)
        }
        studyButton.setOnClickListener {
            selectedCategory = "Study"
            updateCategorySelection(studyButton)
        }
        eventButton.setOnClickListener {
            selectedCategory = "Event"
            updateCategorySelection(eventButton)
        }
    }

    private fun updateCategorySelection(selectedButton: Button) {
        val buttons = listOf(
            findViewById<Button>(R.id.life_button),
            findViewById<Button>(R.id.work_button),
            findViewById<Button>(R.id.study_button),
            findViewById<Button>(R.id.event_button)
        )

        buttons.forEach { button ->
            button.setBackgroundResource(R.drawable.category_button_bg)
        }

        selectedButton.setBackgroundColor(getColor(R.color.selected_category))
    }

    private fun setupBackButton() {
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupDatePicker() {
        val datePickerButton: ImageButton = findViewById(R.id.datePickerButton)
        datePickerButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val dateFormat = SimpleDateFormat("EEEE dd, MMMM", Locale.getDefault())
                    taskDate.text = dateFormat.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun setupTimePicker() {
        val timePickerButton: ImageButton = findViewById(R.id.timePickerButton)
        timePickerButton.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    startTime.text = String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setupCreateButton() {
        createButton.setOnClickListener {
            createTask()
        }
    }

    private fun createTask() {
        val title = taskTitle.text.toString().trim()
        val time = startTime.text.toString()
        val reminder = reminderSwitch.isChecked

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter task title", Toast.LENGTH_SHORT).show()
            return
        }

        if (time == "Select time") {
            Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Task(
            title = title,
            date = calendar.time,
            startTime = time,
            endTime = "",
            category = selectedCategory,
            reminder = reminder
        )

        TaskStorage.saveTask(this, task)

        setResult(RESULT_OK)
        finish()
    }
}